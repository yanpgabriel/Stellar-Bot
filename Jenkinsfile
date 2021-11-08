pipeline {
  agent any
  stages {
    stage('Build') {
      parallel {
        stage('Build') {
          agent {
            docker {
              image 'maven:3.8.3-openjdk-17'
              args '-v /home/maven:/root/.m2'
            }

          }
          steps {
            echo 'Iniciando Build'
            sh 'mvn clean package'
          }
        }

        stage('List') {
          steps {
            sh 'pwd'
            sh 'ls -la'
            sh 'mkdir /home/teste'
          }
        }

      }
    }

    stage('Store .jar') {
      steps {
        archiveArtifacts(artifacts: 'target/Stellar-Bot-*.jar', caseSensitive: true)
      }
    }

  }
}