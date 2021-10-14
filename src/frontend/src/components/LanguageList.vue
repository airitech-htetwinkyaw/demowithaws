<template>
  <div id="wrap">
      
    <!-- <div id="top-title"><h2 id="top-title-inner">フロントエンド言語</h2></div> -->
    <div id="top-left"><router-link to="/home" class="left-link">ホーム</router-link></div>
    <div id="top-right"><h2 id="top-title-inner">{{ pageTitle }}</h2></div>
    <div id="language-column">
      <div v-for="(option, index) in commonLanguagesArray" :key="index">
        <input type="button" :value="option" class="lan-button" @click="showLink(option,index)">
      </div>
    </div>
    <div id="link-column">
      <h2 class="study-link-title">「{{ language }}」の勉強できるお勧めリンク</h2>
      <ul class="link-li" v-for="(link, index) in commonArray" :key="index">
            <li><a :href="link">{{ link }}</a></li>
      </ul>
    </div>
  </div>
</template>

<script>
const pageTitles = ["フロントエンド言語","バックエンド言語","データベース","アマゾンウェブサービス"]
const languagesArrays = [
    ["HTML", "PUG", "CSS", "SCSS", "VUEJS", "REACT", "ANGULAR", "TYPESCRIPT", "NUXTJS", "ATOMIC DESIGN"],
    ["TYPESCRIPT", "NODEJS", "CLEAN ARCHITECTURE", "DEPENDENCY INJECTION"],
    ["MYSQL(AURORADB)", "DYNAMODB"],
    ["S3", "IAM", "CLOUDFRONT", "AURORADB", "LAMBDA", "APPSYNC", "DYNAMODB", "AMPLIFY", "COGNITO", "KINESIS","STEP FUNCTION"]
];

const frontLinkArrays = [
    ["https://www.w3schools.com/html/", "https://www.udemy.com/course/html-css-js/", "https://www.tutorialspoint.com/html/html_overview.htm", "https://www.javatpoint.com/html-tutorial", "https://w3points.com/category/html-tutorial/"],
    ["https://pugjs.org/api/getting-started.html", "https://www.udemy.com/course/pug-template-engine/", "https://www.nodejsera.com/library/pug/pug-introduction.html"],
    ["https://www.w3schools.com/css/", "https://www.udemy.com/course/html-css-js/", "https://www.tutorialspoint.com/css/index.htm", "https://www.javatpoint.com/css-tutorial", "https://w3points.com/category/css-tutorial/"],
    ["https://www.toptal.com/sass/theming-scss-tutorial", "https://docs.gitlab.com/ee/development/fe_guide/style/scss.html"],
    ["https://vuejs.org/", "https://www.udemy.com/course/learn-vuejs/", "https://5balloons.info/vuejs-tutorials-course-introduction/#", "https://www.tutorialspoint.com/vuejs/index.htm", "https://www.javatpoint.com/vue-js","https://bootstrap-vue.org/"],
    ["https://eimaung.com/react/", "https://ja.reactjs.org/docs/getting-started.html", "https://www.udemy.com/course/complete-react-developer-zero-to-mastery/", "https://create-react-app.dev/docs/getting-started/", "https://www.javatpoint.com/reactjs-tutorial"],
    ["https://www.udemy.com/course/learn-angular-2-from-beginner-to-advanced/?", "https://www.w3schools.in/angular/introduction/"],
    ["https://www.udemy.com/course/understanding-typescript-jp/", "https://www.tutorialspoint.com/typescript/index.htm", "https://www.typescriptlang.org/docs/", "https://www.java4coding.com/contents/typescript/typescript-tutorial", "https://www.javatpoint.com/typescript-tutorial"],
    ["https://www.udemy.com/course/nuxtjs-the-complete-guide/", "https://nuxtjs.org/"],
    ["https://qiita.com/d2cd-kimura/items/4aee84da42131f40b808","https://www.welcometothejungle.com/en/articles/atomic-design-front-end-developer"]];

const backLinkArrays = [
    ["https://www.udemy.com/course/ts-for-js-developers/", "https://www.geeksforgeeks.org/how-to-use-typescript-on-backend/"],
    ["https://www.udemy.com/topic/nodejs/", "https://www.tutorialspoint.com/nodejs/index.htm","https://www.javatpoint.com/nodejs-tutorial","https://en.wikipedia.org/wiki/Node.js","https://www.w3schools.com/nodejs/nodejs_intro.asp"],
    ["https://www.youtube.com/watch?v=BvzjpAe3d4g", "https://blog.spacemarket.com/code/clean-architecture-node/","https://whatis.techtarget.com/definition/clean-architecture","https://www.freecodecamp.org/news/a-quick-introduction-to-clean-architecture-990c014448d2/"],
    ["https://en.wikipedia.org/wiki/Dependency_injection", "https://qiita.com/okadabasso/items/066efc2e728a666b8732","https://www.javatpoint.com/dependency-injection-in-spring","https://www.freecodecamp.org/news/a-quick-intro-to-dependency-injection-what-it-is-and-when-to-use-it-7578c84fa88f/"]
    ];

const dbLinkArrays = [
    ["https://docs.aws.amazon.com/ja_jp/AmazonRDS/latest/AuroraUserGuide/Aurora.AuroraMySQL.html", "mysql link 2"],
    ["https://www.udemy.com/course/dynamodb/", "https://www.w3schools.com/whatis/whatis_aws_dynamodb.asp", "https://www.javatpoint.com/aws-dynamodb"]
];

const awsLinkArrays = [
    ["https://airitechjp.udemy.com/course/aws-foundations-amazon-s3-mastery-bootcamp/learn/lecture/13292658#overview", "https://docs.aws.amazon.com/AmazonS3/latest/userguide/Welcome.html","https://www.javatpoint.com/aws-s3"],
    ["https://airitechjp.udemy.com/course/identity-and-access-management-iam/","https://docs.amazonaws.cn/en_us/IAM/latest/UserGuide/introduction.html","https://aws.amazon.com/jp/iam/","https://www.w3schools.com/whatis/whatis_aws_iam.asp","https://www.javatpoint.com/aws-iam"],
    ["https://www.udemy.com/course/master-aws-cloudfront-in-a-weekend/", "https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/Introduction.html", "https://www.w3schools.com/whatis/whatis_aws_cloudfront.asp","https://www.javatpoint.com/aws-cloudfront-cdn"],
    ["https://www.javatpoint.com/aws-aurora", "https://www.w3schools.com/whatis/whatis_aws_aurora.asp"],
    ["https://www.udemy.com/course/aws-lambda-serverless/", "https://en.wikipedia.org/wiki/AWS_Lambda", "https://docs.aws.amazon.com/lambda/latest/dg/welcome.html","https://www.javatpoint.com/aws-lambda"],
    ["https://docs.aws.amazon.com/appsync/latest/devguide/what-is-appsync.html", "https://www.amazonaws.cn/en/appsync/faqs/"],
    ["https://www.udemy.com/course/dynamodb/", "https://www.w3schools.com/whatis/whatis_aws_dynamodb.asp", "https://www.javatpoint.com/aws-dynamodb"],
    ["https://www.udemy.com/course/aws-appsync-amplify-with-react-graphql-course/", "https://qiita.com/takeiin/items/1089f4a0866665de49c6"],
    ["https://docs.aws.amazon.com/ja_jp/cognito/latest/developerguide/what-is-amazon-cognito.html", "https://qiita.com/katekichi/items/dbf279077db6a7b597fc"],
    ["https://www.fenet.jp/aws/column/aws-beginner/457/", "https://www.javatpoint.com/aws-kinesis"],
    ["https://docs.aws.amazon.com/ja_jp/step-functions/latest/dg/welcome.html", "https://qiita.com/miyuki_samitani/items/6c58d107cdc86d938fe3"]
    ];

const allArrays = [frontLinkArrays, backLinkArrays, dbLinkArrays , awsLinkArrays];

export default {
    data(){
        return{
            commonLanguagesArray:[],
            commonArray:[],
            language:"",
            pageTitle:"",
            selectedCategory:""
        }
    },
    methods:{
        showLink(text,index){
            this.language = text; 
            this.commonArray = allArrays[parseInt(this.selectedCategory)][index];      
        }
    },
    mounted(){        
        this.selectedCategory = localStorage.getItem("category");
        this.commonLanguagesArray = languagesArrays[parseInt(this.selectedCategory)];
        this.language = this.commonLanguagesArray[0]
        this.commonArray = allArrays[parseInt(this.selectedCategory)][0];
        this.pageTitle = pageTitles[parseInt(this.selectedCategory)];
    }
}
</script>