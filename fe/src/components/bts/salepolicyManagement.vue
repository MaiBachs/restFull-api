<script setup>
import axios from "axios"
import moment from 'moment'
import { ref, reactive, } from "vue"
import VueDatePicker from "@vuepic/vue-datepicker"
import { useLoaddingStore } from './../../store/LoaddingStore'
import Multiselect from "@vueform/multiselect";
import policyDetail from "@/components/bts/policyDetail.vue"
import policyDelete from "@/components/bts/policyDelete.vue"
import policyEdit from "@/components/bts/policyEdit.vue"
import { notify } from "@kyvg/vue3-notification";
const itemEdit = ref({})
const originItem = ref({})
const loaddingStore = useLoaddingStore()
const listPolicy = ref([]);
const checkListPolicy= ref([]);
const totalRecord = ref(0)
const showSaveButton = ref(false)
const searchData = reactive({
  id: "",
  name: "",
  status: "",
  current_page: 1,
  paging: true,
  sale_time: "",
  start_date: "",
  end_date: "",
  add_sale_time_configs: [
    {
      time_from: "",
      time_to: "",
    }
  ]
});
const fromDate = ref();
const toDate = ref();
//newDate
// const fromDate = ref(new Date());
// const toDate = ref(new Date());
function formatDate(date) {
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
}


const searchPolicy = async () => {
  searchData.start_date = fromDate.value ? moment(fromDate.value).format("DD/MM/yyyy") : "";
  searchData.end_date = toDate.value ? moment(toDate.value).format("DD/MM/yyyy") : "";
  listPolicy.value = [];
  checkListPolicy.value = true;
  try {
  loaddingStore.loading = true;
  const response = await axios.post(
      'api/map-sale-policy/search',{
      id: searchData.id,
      name: searchData.name,
      status: searchData.status,
      current_page: searchData.current_page,
      sale_time: searchData.sale_time,
      start_date: searchData.start_date,
      end_date: searchData.end_date,
      add_sale_time_configs: searchData.add_sale_time_configs,
      paging: true,
      });
    if (response && response.data.data.plan_sale_policy_dtos && response.data.data.plan_sale_policy_dtos.length > 0) {
      const responseData = response.data.data.plan_sale_policy_dtos;
      console.log(formatDate(new Date(responseData.start_date)));
      responseData.start_date = formatDate(new Date(responseData.start_date));
      responseData.end_date = formatDate(new Date(responseData.end_date));
      listPolicy.value = response.data.data.plan_sale_policy_dtos;
      totalRecord.value = response.data.data.total_record;
      searchData.current_page = response.data.data.current_page;
      paginate.total_page = response.data.data.total_page;
      paginate.total_record = response.data.data.total_record;
    } else {
      checkListPolicy.value = false;
    }
  loaddingStore.loading = false;
} catch (error) {
    loaddingStore.loading = false;
    notify({
      type: "error",
      title: "Get current user",
      text: error,
      duration: 500,
    });
  }
}
const getPolicyDetail = ref({
  name: 0,
  content: 0,
  status: 0,
  start_date: 0,
  end_date: 0,
  sale_time: 0,
});
const checkDataDetail = ref(false)
var ListSaleTime = ref([])
const delete_sale_time_configs = ref ([]);
const add_sale_time_configs = ref ([]);
const  idsToDelete = ref ([]);
console.log(idsToDelete);
async function getDetail (item)  {
  loaddingStore.loading = true;
    const response = await axios.post("/api/map-sale-policy/detail", {id: item.id });
    if(item) {
      getPolicyDetail.value = item;
      loaddingStore.loading = false;
    } else {
    checkDataDetail.value = true
  }
  ListSaleTime.value = response.data.sale_time_config_dtos
  console.log(ListSaleTime.value);
}
const getEdit = async (item,data) => {
  idsToDelete.value = [];
  delete_sale_time_configs.value = [];
  add_sale_time_configs.value = [];
  console.log(delete_sale_time_configs.value );
  console.log(add_sale_time_configs.value );
  console.log(idsToDelete.value );
  itemEdit.value = [];
  itemEdit.value = data;
  originItem.value = { ...data }
  loaddingStore.loading = true
  showSaveButton.value = false
  const response = await axios.post("/api/map-sale-policy/detail", {id: item.id });
  loaddingStore.loading = false
  if (item) {
    getPolicyDetail.value = JSON.parse(JSON.stringify(item));
  } else {
    checkDataDetail.value = true
  }
  ListSaleTime = response.data.sale_time_config_dtos
  console.log(ListSaleTime);
};
const dateTime = (date) => {
  var getDate = ref("");
  if (date) {
    getDate.value = moment(date).format("DD-MM-yyyy");
    return getDate.value;
  } else {
    return getDate.value;
  }
};
const options = [
  {
    title: "Active",
    value: "1",
  },
  {
    title: "Inactive",
    value: "0",
  },
];
// const canOk = computed(() => JSON.stringify(itemEdit.value) !== JSON.stringify(originItem.value))

const paginate = reactive ({
  total_page:"",
  current_page:"",
  total_record: ""
})
const getQuestionDelete = async (item) => {
  console.log(item.id);
  if (item) {
    getPolicyDetail.value.id = item.id;
  } else {
    checkDataDetail.value = true
  }
  console.log(getPolicyDetail.value);
};
function clearListPolicy() {
  searchPolicy()
}
function myCallback(newPage) {
  searchPolicy(newPage)
}
</script>

<template>
  <div class="card-body p-0">
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
          >{{ $t("POLICY") }}:</label>
          <div class="col-sm-8">
            <input type="text"
                v-model="searchData.name"
                track-by="name"
                style="min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 100%;border: var(--ms-border-width,1px) solid var(--ms-border-color,#d1d5db);border-radius: var(--ms-radius,4px); padding-left: 13px;font-size: initial;font-weight: 500;"
            />
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
          >{{ $t("STATUS") }}:</label>
          <div class="col-sm-8">
            <multiselect
              v-model="searchData.status"
              deselect-label="Can't remove this value"
              track-by="title"
              label="title"
              :placeholder="$t('SELECT_ONE')"
              :options="options"
              :searchable="false"
              :allow-empty="false">
          </multiselect>
          </div>
        </div>
      </div>
    </div>
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("FROM_DATE") }}:</label>
          <div class="col-sm-8">
            <VueDatePicker
                v-model="fromDate"
                auto-apply
                format="dd-MM-yyyy"
            ></VueDatePicker>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("TO_DATE") }}:</label>
          <div class="col-sm-8">
            <VueDatePicker
                v-model="toDate"
                auto-apply
                format="dd-MM-yyyy">
          </VueDatePicker>
          </div>
        </div>
      </div>
    </div>
    <div class="row mb-3">
      <div class="col align-self-start">
      </div>
      <div class="col align-self-start">
      </div>
    </div>
    <div class="col-md-6">
    </div>
    <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
            class="btn btn-pill btn-primary-gradien"
            type="button"
            @click="searchPolicy">{{ $t("SEARCH") }}
        </button>
      </div>
    </div>
    <div class="row mb-3 evaluations">
      <fieldset v-if="!checkListPolicy" class="table-responsive">
        <div class="text-center no-item">{{$t("NO.ITEM")}}</div>
      </fieldset>
      <fieldset v-if="listPolicy.length" class="table-responsive">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead class="table-success" style="background-color: #24695c; color: #fff;">
            <tr>
              <th scope="col">#</th>
              <th scope="col">{{ $t("POLICY") }}</th>
              <th scope="col" style="width: 600px">{{ $t("SALES_TIME") }}</th>
              <th scope="col">{{ $t("STATUS") }}</th>
              <th scope="col">{{ $t("FROM_DATE") }}</th>
              <th scope="col">{{ $t("TO_DATE") }}</th>
              <th scope="col" style="width: 150px">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
            </tr>
            </thead>
            <tbody v-for="(item, index) in listPolicy" :key="item">
            <tr>
              <td>{{(searchData.current_page-1)*10 +(index+1)}}</td>
              <td>{{ item.name }}</td>
              <td>{{ item.sale_time }}</td>
              <td>{{ item.status == 0 ? "Inactive" : "Active"}}</td>
              <td>{{ dateTime(item.start_date) }}</td>
              <td>{{ dateTime(item.end_date) }}</td>
              <td>
                <div class="buttons">
                  <button
                      class="btn btn-primary btn-xs"
                      data-bs-toggle="modal"
                      data-original-title="test"
                      data-bs-target="#exampleModalDetailPolicy"
                      @click="getDetail(item)">
                  <i class="icofont icofont-ui-note"></i>
                  </button>
                  <button
                      class="btn btn-primary btn-xs"
                      data-bs-toggle="modal"
                      data-original-title="test"
                      data-bs-target="#exampleModalEditlPolicy"
                      @click="getEdit(item,data)"
                      style="margin-left: 5px;">
                  <i class="icofont icofont-edit"></i>
                  </button>
                  <button
                    v-if="item.name !== 'Bts normal'"
                    style="background-color: rgb(188, 22, 22) !important; margin-left: 5px;"
                    class="btn btn-primary btn-xs"
                    data-bs-toggle="modal"
                    data-original-title="test"
                    data-bs-target="#exampleModalDeletee"
                    @click="getQuestionDelete(item)">
                    <i class="icofont icofont-ui-delete"></i>
                </button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
          <div class="pagination-search">
              <pagination v-model="searchData.current_page" :records="totalRecord" :per-page="10" @paginate="myCallback"/>
            </div>
        </div>
      </fieldset>
    </div>
    <policyDetail 
    v-model="getPolicyDetail" 
    :options="options" 
    :listPolicy="listPolicy" 
    :ListSaleTime="ListSaleTime" />
    <policyDelete 
    @clearValue="clearListPolicy" 
    v-model="getPolicyDetail" 
    :item="item" />
    <policyEdit 
    @clearValue="clearListPolicy" 
    :itemAdd="getPolicyDetail" 
    v-model="getPolicyDetail" 
    :item="item" 
    :options="options" 
    :listPolicy="listPolicy" 
    :ListSaleTime="ListSaleTime"
    :start_date="getPolicyDetail.start_date"
    :end_date="getPolicyDetail.end_date"/>
    
  </div>
</template>

<style lang="scss" scoped>
.multiselect {
  max-height: 35px;
  min-height: 30px;
}
.dp__input_wrap {
  height: 35px;
  display: flex;
}
.evaluations {

  fieldset {
    padding: 15px;
    margin: 0 2px;
    border: 1px solid #c0c0c0;
  }
  legend {
    float: initial;
    font-size: 14px;
    width: auto;
    border-bottom: none;
    padding: 0px 5px;
  }
  .no-item {
    font-weight: 500;
  }
  svg {
    width: 16px;
    height: 16px;
  }
}
</style>
