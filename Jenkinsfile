pipeline {
  agent {
    docker {
      image 'maven:3.8.3-openjdk-17'
      args '-v /home/.m2:/root/.m2'
    }

  }
  stages {
    stage('Build') {
      parallel {
        stage('Iniciar') {
          steps {
            echo 'Iniciando Build'
          }
        }

        stage('Criar arquivo .jar') {
          steps {
            sh 'mvn clean package'
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
