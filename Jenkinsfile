pipeline {
  agent any
  stages {
    stage('Compile') {
      steps {
        sh 'mvn clean install'
      }
    }
    stage('archive') {
      steps {
        junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true)
      }
    }
  }
}