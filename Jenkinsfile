pipeline {
    agent any

    environment {
        REGISTRY = "10.13.4.165:5500"
        IMAGE_NAME = "contact"
        IMAGE_TAG = "latest"

        REPO_URL = "git@github.com:bag234/InfoStorage.git"
        REPO_BRANCH = "main"

        REMOTE_DIR = "contact"
        SSH_SERVER = "by-ssh"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: "${REPO_BRANCH}", credentialsId: '9ef5277f-8146-4070-863e-73936eab46e7', url: "${REPO_URL}"
            }
        }

        stage ('java build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build & Push') {
            steps {
                script {
                    def app = docker.build("${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}")
                    docker.withRegistry("http://${REGISTRY}", "") {
                        app.push()
                    }
                }
            }
        }

        stage('Deploy') {
    steps {
        sshPublisher(
            publishers: [
                sshPublisherDesc(
                    configName: "${SSH_SERVER}",
                    transfers: [
                        sshTransfer(
                            sourceFiles: "docker-compose.yaml",
                            remoteDirectory: "${REMOTE_DIR}",
                            execCommand: """
                                mkdir -p ${REMOTE_DIR} && cd ${REMOTE_DIR} && docker compose pull &&  docker compose up -d --force-recreate
                            """
                        ),
                        sshTransfer(
                            sourceFiles: "nginx.conf",
                            remoteDirectory: "${REMOTE_DIR}",
                            execCommand: """
                                mv ~/${REMOTE_DIR}/nginx.conf /etc/nginx/sites-available/${REMOTE_DIR}.conf && \
                                ln -sf /etc/nginx/sites-available/${REMOTE_DIR}.conf /etc/nginx/sites-enabled/${REMOTE_DIR}.conf && \
                                sudo nginx -t && sudo nginx -s reload
                            """
                        )
                    ],
                    usePromotionTimestamp: false,
                    useWorkspaceInPromotion: false,
                    verbose: true
                )
            ]
        )
    }
}
    }

    post {
        always {
            echo 'Pipeline finished'
        }
        failure {
            echo 'Pipeline failed'
        }
    }
}
