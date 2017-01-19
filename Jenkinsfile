#!groovy
node {
    def jdk8_docker_image = 'maven:3.3.3-jdk-8'

    checkout scm

    docker.image(jdk8_docker_image).inside {
        stage('build') {
            sh "mvn --batch-mode clean package"
            junit '**/target/surefire-reports/TEST-*.xml'
        }
    }
}
