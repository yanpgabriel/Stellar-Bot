pipeline {
  agent any
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

  }
}