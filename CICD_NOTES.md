# CI/CD Notes — Brain404 Hotel Backend

## Trigger Rule

The pipeline runs automatically on every **push to `main`** (including merged pull requests).

```yaml
on:
  push:
    branches:
      - main
```

This means any commit that lands on `main` — direct push or a merged PR — immediately kicks off the three-job pipeline.

---

## Image Versioning / Tagging

Each Docker image is pushed to ECR with **two tags**:

| Tag | Value | Purpose |
|-----|-------|---------|
| Commit SHA | `abc1234...` (full 40-char SHA) | Immutable pointer to exact code |
| `latest` | always updated | Convenience / quick inspection |

The ECS task definition is updated with the **SHA-tagged** image, not `latest`.  
This ensures ECS always pulls the exact image that was built for this commit, and every ECR image is permanently traceable to the Git commit that produced it.

---

## How to Roll Back (Step-by-Step)

If the new deployment is broken and you need to revert to the previous working version:

### Option A — Re-deploy the previous ECS task definition revision (fastest)

1. Open the **AWS Console → ECS → Clusters → hotel-cluster → Services → hotel-service**.
2. Click **Update service**.
3. Under **Task definition**, change the **Revision** to the previous number (e.g. if current is `hotel-task:7`, choose `hotel-task:6`).
4. Click **Update** and wait for the service to stabilize.
5. Verify by calling `GET /actuator/health` — it should return `{"status":"UP"}`.

### Option B — Re-deploy via AWS CLI (from your terminal)

```bash
# 1. List recent revisions to find the last good one
aws ecs list-task-definitions \
  --family-prefix hotel-task \
  --sort DESC \
  --region us-east-1

# 2. Roll back to the previous revision (replace 6 with the target revision number)
aws ecs update-service \
  --cluster hotel-cluster \
  --service hotel-service \
  --task-definition hotel-task:6 \
  --region us-east-1

# 3. Wait until stable
aws ecs wait services-stable \
  --cluster hotel-cluster \
  --services hotel-service \
  --region us-east-1

# 4. Confirm health
curl -s http://hotel-alb-384312504.us-east-1.elb.amazonaws.com/actuator/health
```

### Option C — Re-trigger the pipeline at an older commit

```bash
git revert HEAD          # creates a new commit that undoes the bad commit
git push origin main     # triggers the pipeline — tests + build + deploy of the reverted code
```

This is the safest option because it goes through the full CI pipeline (tests pass before deploying).

---

## GitHub Secrets Required

Add these in **GitHub → Settings → Secrets and variables → Actions**:

| Secret | Description |
|--------|-------------|
| `AWS_ACCESS_KEY_ID` | IAM user access key |
| `AWS_SECRET_ACCESS_KEY` | IAM user secret key |

> **DB credentials are NOT stored in GitHub.** They remain in AWS Secrets Manager and are injected into the ECS task at runtime — the pipeline only handles build + image push + redeploy.

---

## Minimum IAM Permissions for the CI/CD User

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ecr:GetAuthorizationToken",
        "ecr:BatchCheckLayerAvailability",
        "ecr:InitiateLayerUpload",
        "ecr:UploadLayerPart",
        "ecr:CompleteLayerUpload",
        "ecr:PutImage"
      ],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "ecs:DescribeTaskDefinition",
        "ecs:RegisterTaskDefinition",
        "ecs:UpdateService",
        "ecs:DescribeServices"
      ],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": "iam:PassRole",
      "Resource": "arn:aws:iam::*:role/ecsTaskExecutionRole"
    }
  ]
}
```
