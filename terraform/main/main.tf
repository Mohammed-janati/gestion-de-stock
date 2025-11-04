terraform {
  backend "s3" {
    bucket         = "my-dynamic-tf-state"
    key            = "infra/main/terraform.tfstate"
    region         = "af-south-1"
    use_lockfile  = true
    encrypt        = true
  }


  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}

# Configure the AWS Provider
provider "aws" {
  region = var.region

}

