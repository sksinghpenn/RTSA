pipeline {
     agent any
     environment {
         FIRST_NAME='SK'
         LAST_NAME='Singh'
         my_secret=credentials('MY_NAME_SECRET')

     }



     stages {
         stage('clean') {
             steps {
             /*
                 sh 'echo $FIRST_NAME $LAST_NAME $my_secret'


                 sh '''
                     echo "by the way i can do more stuff here"
                     echo -las
                     '''
                     */
                /*
                retry(3) {
                     sh 'i am good goy'
                 }
                 */
                 /*
                 timeout(time: 3,unit: 'SECONDS') {
                     sh 'sleep 5'
                 }
                 */

                sh 'mvn clean '
             }
         }

         stage('build'){
             sh 'mvn package'
         }
     }

     post {

         always {
             echo 'i will always get executed'
         }
         success {
             echo 'I will only get executed if it succeeds'
         }
         failure {
             echo 'I will only get exetuted it it fails'
         }
         unstable {
             echo 'I will only get executed if this is unstable'
         }
     }

 }
