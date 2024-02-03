import { createApp } from 'vue'
import { createPinia } from 'pinia'
import axios from 'axios'
import VueAxios from 'vue-axios'
import App from './App.vue'
import router from './router'
import store from './store'
import Multiselect from 'vue-multiselect'
import '@vueform/multiselect/themes/default.css'
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import i18n from './i18n'
import VueFeather from "vue-feather";
import Breadcrumbs from './layout/breadCrumbs.vue';
import Pagination from 'v-pagination-3';
import Notifications from '@kyvg/vue3-notification'
import Autocomplete from 'vue3-autocomplete'
import 'vue3-autocomplete/dist/vue3-autocomplete.css'
import  { ModelSelect } from 'vue-search-select';


import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap';
import 'bootstrap/dist/js/bootstrap.min.js';
import './assets/scss/app.scss';
	
createApp(App)
.component('pagination', Pagination)
.use(store)
.use(router)
.use(createPinia())
.use(VueAxios, axios)
.use(Notifications)
.use(i18n)
.component('Autocomplete', Autocomplete)
.component('Breadcrumbs', Breadcrumbs)
.component(VueFeather.name, VueFeather)
.component('multiselect', Multiselect)
.component('Datepicker', Datepicker)
.component('ModelSelect', ModelSelect)
.mount('#app')
export { i18n };

