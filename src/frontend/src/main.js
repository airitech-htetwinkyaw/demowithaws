import Vue from 'vue'
import App from './App.vue'
import router from './router'
import 'bootstrap/dist/css/bootstrap.min.css'
import Vuelidate from 'vuelidate'
import VueSimpleAlert from "vue-simple-alert";
import VTooltip from 'v-tooltip';
import { Row, Column } from 'vue-grid-responsive';

//テストのため追加
//import 'mutationobserver-shim'// Amplify読み込み
import Amplify, * as AmplifyModules from 'aws-amplify'
import { AmplifyPlugin } from 'aws-amplify-vue'
import awsconfig from './aws-exports'

//CSVダウンロードのため
import JsonCSV from 'vue-json-csv'
 
Vue.component('downloadCsv', JsonCSV)

Amplify.configure(awsconfig)

Vue.use(AmplifyPlugin, AmplifyModules)

Vue.component('row', Row);
Vue.component('column', Column);

Vue.use(VueSimpleAlert);
Vue.use(Vuelidate);
Vue.use(VTooltip);

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
