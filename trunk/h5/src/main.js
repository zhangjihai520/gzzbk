import Vue from 'vue'
import MintUI from 'mint-ui'
import 'mint-ui/lib/style.css'
import App from './App'
import router from './router'
import { Toast,Indicator } from 'mint-ui';
import "mint-ui/lib/style.css"
import './permission'
// Vue.prototype.$toast = Toast;
Vue.prototype.toast = function (msg) {
  Toast({
    message:msg,
    position: "bottom",
    duration: 4000,
  })
}
Vue.prototype.loading = Indicator;
Vue.use(MintUI);
Vue.config.productionTip = false


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
