pipeline {
    agent any

    environment {
        // You can set Terraform version or path if needed
        TF_VERSION = "1.5.0"
        // Example: path to Terraform binary; adjust if installed differently
        TF = "terraform"
    }

    stages {
        stage('Checkout') {
            steps {
                // clone your repository
                git url: 'https://github.com/BagadeSaurav/terraform.git', branch: 'main'
            }
        }

        stage('Terraform Init') {
            steps {
                sh """
                  ${main.tf} init
                """
            }
        }

        stage('Terraform Plan') {
            steps {
                sh """
                  ${main.tf} plan -out=tfplan.out
                """
            }
        }

        stage('Terraform Apply') {
            steps {
                // You might want to require approval for apply in production
                input message: "Proceed with terraform apply?"
                sh """
                  ${main.TF} apply -auto-approve tfplan.out
                """
            }
        }
    }

    post {
        success {
            echo "Terraform apply succeeded"
        }
        failure {
            echo "Pipeline failed"
        }
    }
}
