<script setup>
import moment from 'moment'
import axios from "axios";
import { ref,watch } from "vue";
import { reactive } from "vue"
import { i18n } from '../../main.js';
import { notify } from "@kyvg/vue3-notification";
import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
import { getGroups} from './../../store/getGroup'
import { getChannelType } from './../../store/getChannelType'
import { getJobsToPlan} from './../../store/getJodChannel'
import { useItemConfigStore } from './../../store/ItemConfigStore';
import { useLoaddingStore } from './../../store/LoaddingStore'
import { getGravedad } from './../../store/getListGravedad'
import useEvaluation from "@/composables/useEvaluation"
import Modal from '@/components/common/customModal.vue'
import deleteItemEvaluation from '@/components/common/questionModal.vue';
const loaddingStore = useLoaddingStore()
const { addEvaluationQueue, evaluationQueue} = useEvaluation(ref)
const itemConfigStore = useItemConfigStore();
const listGroup = getGroups();
const listChannelType = getChannelType();
const listJod = getJobsToPlan();
const getListGravedad= getGravedad();
const getContentImg = ref('')
const inputFile = ref(null);
const addGravedad = ref("");
const addReason = ref("")
const reason_list = ref([])
const itemDelete = ref("")
const eventDelete = ref("")
const noChangeReasonList = ref([])
const showPopupQuestionDelete = ref(false)
const getItemDetail = ref({
  ok: false,
  nok: false,
  na: false,
  photo: false,
  audio: false,
  file: false,
  video: false
});
const checkListEvalution = ref(true);
const dataSearch = ref([]);
const getItemsDetailCheck = ref(false)
const evaluationName = ref();

const searchData = reactive({
  group_id: "",
  group_code: "",
  channel_type_id: "",
  evaluation_id: "",
  from_date: null,
  to_date: null,
  current_page: 1,
});
// const item = reactive({
//   gravedad_code: "",
//   reason_id: ""
// })
const paginate = reactive ({
  totalPage:"",
  currentPage:"",
  totalRecord: ""
})
// const fromDate = ref(new Date());
// fromDate.value = moment(new Date()).subtract(1, 'months');
// const toDate = ref(new Date());
const fromDate = ref();
const toDate = ref();
const validated = ref(false)
const fileAddImage = ref([])

const searchItemEvaluation = async () => {
  getItemDetail.value = [];
  getItemsDetailCheck.value = false
  searchData.from_date = fromDate.value ? moment(fromDate.value).format("DD-MM-yyyy") : null;
  searchData.to_date = toDate.value ? moment(toDate.value).format("DD-MM-yyyy") : null;
  if (toDate.value && fromDate.value > toDate.value) {
    notify({
      type: "error",
      title: i18n.global.t('HOME.SEARCH.ERROR.FROMDATE_BEFORE_TODATE'),
      duration: 2000,
    });
  return 
  } else {
    try {
        checkListEvalution.value = true
        loaddingStore.loading = true;
        const response = await axios.post(
          "/api/item-config/search",
          searchData
        );

        if(response && response.data.data.item_config_dto_list && response.data.data.item_config_dto_list.length) {
            checkListEvalution.value = true
            dataSearch.value = response.data.data.item_config_dto_list
            paginate.totalPage = response.data.data.total_page;
            paginate.totalRecord = response.data.data.total_record;
        }else{
          checkListEvalution.value = false
          dataSearch.value = [];
        }
          loaddingStore.loading = false;
  } catch (error) {
    loaddingStore.loading = false;
    console.log(error);
    notify({
      type: "error",
      title: "Get current user",
      text: error,
      duration: 500,
    });
  }
  }

  // itemConfigStore.searchItem(searchData);
  // paginate.totalPage = itemConfigStore.paginate.totalPage;
  // paginate.totalRecord = itemConfigStore.paginate.totalRecord;
};
function myCallback(newPage) {
  searchItemEvaluation(newPage)
}
const downloadFile = async () => {
  searchData.from_date = fromDate.value ? moment(fromDate.value).format("DD-MM-yyyy") : null;
  searchData.to_date = toDate.value ? moment(toDate.value).format("DD-MM-yyyy") : null;
  itemConfigStore.export(searchData)
}


const getDetail = async (itemConfig) => {
  var y = document.documentElement.scrollHeight;
  addReason.value = null;
  addGravedad.value = null;
  evaluationQueue.value = []
  getItemDetail.value = []
  noChangeReasonList.value = []
  validated.value = false
  getItemsDetailCheck.value = false
  evaluationName.value = itemConfig;
  loaddingStore.loading = true;
  const detail = await axios.get(`/api/item-config/detail-item-config?itemConfigId=${itemConfig.id}`);
  if(detail && detail.data.data) {
    getItemsDetailCheck.value = true
    getItemDetail.value = {
      ...detail.data.data,
      ok: !!detail.data.data.ok,
      nok: !!detail.data.data.nok,
      na: !!detail.data.data.na,
      photo: !!detail.data.data.photo,
      audio: !!detail.data.data.audio,
      file: !!detail.data.data.file,
      video: !!detail.data.data.video
    }
    window.scrollTo(0, y);
    loaddingStore.loading = false;
  }else {
      getItemsDetailCheck.value = false
    }
    noChangeReasonList.value = getItemDetail.value.reason_list
};

// const suggestedGroups = ref([]);
const searchInput = ref("");

// Phương thức để gợi ý các nhóm dựa trên giá trị nhập vào
// const suggestGroups = () => {
//   if (searchInput.value) {
//     const filteredGroups = listGroup.listGroup.filter((group) =>

//       group.value.code.toLowerCase().includes(searchInput.value.toLowerCase())
//     );
//     suggestedGroups.value = filteredGroups.map((group) => group.value);
//   } 
//   else {
//     suggestedGroups.value = [];
//   }
// };

// const selectGroup = (group) => {
//   searchData.group_id = group.id;
//   searchData.group_code = group.code;
//   suggestedGroups.value = [];
// };

watch(searchData, () => {
  searchInput.value = searchData.group_code;
  if(searchInput.value == ""){
    searchData.group_id = "";
  }
});
function editItem() {
  validated.value = true
  // for (var i in getListGravedad.getListGravedad[i]){
  //     if (getListGravedad.getListGravedad[i].value == item.gravedad_code) {
  //       item.gravedad_code = getListGravedad.getListGravedad[i];
  //     }
  //   }
  //   for (var o in getListGravedad.getListReason[o]){
  //     if (getListGravedad.getListReason[o].value == item.reason_id) {
  //       item.reason_id = getListGravedad.getListReason[o];
  //     }
  //   }

}

const saveEdit = async() => {
  console.log(getItemDetail.value.reason_list)
  var checkParam = false
  var checkListReson = false
  addEvaluationQueue({...getItemDetail.value})
  if(evaluationQueue.value && evaluationQueue.value.length) {
    if(evaluationQueue.value[0].nok === false) {
      var noChangeResonList = []
      if(noChangeReasonList.value && noChangeReasonList.value.length ) {
        for (var i in noChangeReasonList.value){
          if (noChangeReasonList.value[i].created_date) {
            noChangeReasonList.value[i].status = 0
            noChangeResonList.push(noChangeReasonList.value[i])
          }
        }
      }

    }

    const param = {
      ok: evaluationQueue.value[0].ok ? 1 : 0,
      nok: evaluationQueue.value[0].nok ? 1 : 0,
      na: evaluationQueue.value[0].na ? 1 : 0,
      id: getItemDetail.value.id,
      photo: evaluationQueue.value[0].photo ? 1 : 0,
      video: evaluationQueue.value[0].video ? 1 : 0,
      audio: evaluationQueue.value[0].audio ? 1 : 0,
      file: evaluationQueue.value[0].file ? 1 : 0,
      reason_list: reason_list.value || []
    }
    
    if(fileAddImage.value && fileAddImage.value.length) {
      param.file_dto = fileAddImage.value[0]
    } else {
      param.file_dto = {}
    }
    if(evaluationQueue.value[0].nok == 1) {
      param.reason_list = reason_list.value
    }else {
      param.reason_list = noChangeResonList
    }

    console.log(param.reason_list)

    if(getItemDetail.value.reason_list && getItemDetail.value.reason_list.length ) {
      for (var a in getItemDetail.value.reason_list){
        if (getItemDetail.value.reason_list[a].status == 1) {
           checkListReson = true
           break
        }
      }
    }
    
    if(param.ok == 0 && param.nok == 0 && param.na == 0){
      checkParam = false
      notify({
            type: 'error',
            title: i18n.global.t('YOU_MUST_CHOOSE_OPTION_OF_ANSWER'),
            duration: 10000,
          });

    }
    else {
      if(param.nok == 1 && checkListReson == false) {
        checkParam = false
        notify({
            type: "error",
            title: "Get current user",
            text: i18n.global.t('ITEM_EVALUTION_REASON_NOK'),
            duration: 500,
          });
      } else {
        checkParam = true
      }
      
    }

    

    if(checkParam) {
      loaddingStore.loading = true;
      try {
        const response = await axios.put("/api/item-config/edit", param);
        console.log('Kết quả:', response.data);
        validated.value = false
        reason_list.value = []
        notify({
              type: 'success',
              title: i18n.global.t('EDIT_SUCCESSS'),
              duration: 10000,
            });
            getDetail(getItemDetail.value)
      } catch (error) {
        validated.value = true
        notify({
              type: 'error',
              title: error,
              duration: 10000,
            });
      }
    }
    
  //   getItemDetail.value = {
  
  //   ...getItemDetail.value,
  //   ok: !!getItemDetail.value.ok,
  //   nok: !!getItemDetail.value.nok,
  //   na: !!getItemDetail.value.na,
  //   photo: !!getItemDetail.value.photo,
  //   audio: !!getItemDetail.value.audio,
  //   file: !!getItemDetail.value.file,
  //   video: !!getItemDetail.value.video
  // }
    loaddingStore.loading = false;
  }
  }
const showPopupViewImg = ref(false)
  async function viewImage(url) {
    showPopupViewImg.value = false
    getContentImg.value = ""
    fileName.value = ""
    try {
        loaddingStore.loading = true;
        
        const response = await axios.get(`/api/item-config/getImageFromFtp?filePath=${url}`);

        console.log(response)
        if(response.data){

          getContentImg.value = response.data.data.content
          fileName.value = response.data.data.file_name
          showPopupViewImg.value = true
        }
          loaddingStore.loading = false;
        } catch (error) {
          loaddingStore.loading = false;
          console.log(error);
          notify({
            type: "error",
            title: "Get current user",
            text: error,
            duration: 500,
          });
        }
    
  }

  function previewFiles(event) {
    fileAddImage.value = []
    const inputFile = event.target;
    if (!inputFile.files.length) {
      return;
    }

    const file = inputFile.files[0];
    const fileName = file.name;
    const reader = new FileReader();

    reader.onloadend = () => {
      // Dữ liệu base64 được lưu trong reader.result
      const base64String = reader.result;

      // Ở đây, bạn có thể làm gì đó với base64String, ví dụ: hiển thị ảnh, gửi lên máy chủ, v.v.
      var add = { file_name: fileName, content: base64String };
      fileAddImage.value.push(add)
    };

    reader.readAsDataURL(file);
  }
function changePhoto() {
  if(getItemDetail.value.photo == false) {
    getItemDetail.value.audio = false
    getItemDetail.value.video = false
    getItemDetail.value.file = false
  } 
}
function changeAudio() {
  if(getItemDetail.value.audio == false) {
    getItemDetail.value.photo = false
    getItemDetail.value.video = false
    getItemDetail.value.file = false
  } 
}
function changeVideo() {
  if(getItemDetail.value.video == false) {
    getItemDetail.value.photo = false
    getItemDetail.value.audio = false
    getItemDetail.value.file = false
  } 
}
function changeFile() {
  if(getItemDetail.value.file == false) {
    getItemDetail.value.photo = false
    getItemDetail.value.audio = false
    getItemDetail.value.video = false
  } 
}

function addList() {
  console.log(addReason.value)
  console.log(addGravedad.value)
  var checkDuplicate = false
  if(getItemDetail.value.reason_list && getItemDetail.value.reason_list.length) {
    if (getItemDetail.value.reason_list && Array.isArray(getItemDetail.value.reason_list)) {
    getItemDetail.value.reason_list.forEach(function(item) {
        if (item.status == 1 && item.reason_id === addReason.value) {
            checkDuplicate = true;
            notify({
                type: "error",
                text: i18n.global.t('DUPLICATE_RESONS'),
                duration: 800,
            });
            addReason.value = "";
            addGravedad.value = "";
            return;
        }
    });
}
    
  }
  if(checkDuplicate === false && addReason.value && addGravedad.value) {
      for (var i in getListGravedad.listAddGravedad){
      if (getListGravedad.listAddGravedad[i].code == addGravedad.value) {
          addGravedad.value = getListGravedad.listAddGravedad[i];
      }
    }
    for (var o in getListGravedad.listAddReason){
      if (getListGravedad.listAddReason[o].reason_id == addReason.value) {
          addReason.value = getListGravedad.listAddReason[o];
      }
    }
    if(addReason.value &&  addGravedad.value) {
      var addList = {reason_id : addReason.value.reason_id,status: 1,gravedad: addGravedad.value.value ,gravedad_code :addGravedad.value.code}
      var addGetItemDetail = {name : addReason.value.name, gravedad: addGravedad.value.value, reason_id : addReason.value.reason_id, gravedad_code: addGravedad.value.code, status: 1}
      reason_list.value.push(addList)
      getItemDetail.value.reason_list.push(addGetItemDetail)
      return 
    }
  }
  addReason.value = null;
  addGravedad.value = null;
}

function deleteResonList() {
  if(itemDelete.value) {
    itemDelete.value.status = 0
    reason_list.value.push(itemDelete.value)
  }
  deleteRow(eventDelete.value);
  showPopupQuestionDelete.value = false
  deleteRow(event);
}

function showPopupDelete(item,event) {
  itemDelete.value = item
  eventDelete.value = event
  showPopupQuestionDelete.value = true
}

function deleteRow(event) {
  var row = event.target.closest('tr');
  if (row) {
    var table = row.closest('table');
    if (table) {
      var rowIndex = row.rowIndex;
      if (rowIndex >= 0) {
        table.deleteRow(rowIndex);
      }
    }
  }
}

function changeNok() {
  reason_list.value = []
  var getDataReson = getItemDetail.value.reason_list
  getItemDetail.value.reason_list = []
  if (getDataReson && getDataReson.length) {
    getDataReson.forEach(function(item) {
        if (item.created_date) {
          if (item.status === 0) {
                item.status = 1; // Gán lại status bằng 1 nếu status ban đầu là 0
            }
            getItemDetail.value.reason_list.push(item);
        }
    });

    // Xóa các phần tử không có created_date
    getItemDetail.value.reason_list = getItemDetail.value.reason_list.filter(function(item) {
        return item.created_date;
    });
}
}
function downloadFileImage() {
    const downloadLink = document.createElement('a');
    downloadLink.href = getContentImg.value;
    downloadLink.download = fileName.value;
    downloadLink.click();
}
const fileName = ref("")
</script>

<template>
  <div class="card-body p-0">
    <div class="row mb-3">
      
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.channel_type_id"
              :options="listChannelType.listChannelType || []"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("EVALUATION_TYPE") }}</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.evaluation_id"
              :options="listJod.getJobsToPlan|| []"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("GROUP_ITEMS") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.group_code"
              :options="listGroup.listGroup|| []"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
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
              format="dd-MM-yyyy"
            ></VueDatePicker>
          </div>
        </div>
      </div>
      <div class="col align-self-start"></div>
    </div>

    <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
          class="btn btn-pill btn-primary-gradien"
          type="button"
          @click="searchItemEvaluation"
        >
          {{ $t("SEARCH") }}
        </button>
        <button
          class="btn btn-pill"
          style="background-color: #4682b4; color: #fff"
          type="button"
          @click="downloadFile"
        >
          {{ $t("EXPORT_DETAIL") }}
        </button>
      </div>
    </div>

    <div class="row mb-3">
      <fieldset v-if="!checkListEvalution" class="table-responsive">
        <div class="text-center no-item">{{$t("NO.ITEM")}}</div>
      </fieldset>
      <div v-if="dataSearch.length">
        <!-- <div class="row mb-3"> -->
          <fieldset>
            <table class="table table-bordered">
              <thead class="table-success">
                <tr>
                  <th scope="col">#</th>
                  <th scope="col">{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}</th>
                  <th scope="col">{{ $t("EVALUATION") }}</th>
                  <th scope="col">{{ $t("GROUP_ITEMS") }}</th>
                  <th scope="col">{{ $t("ITEMS_EVALUATION") }}</th>
                  <th scope="col">{{ $t("CREATED_DATE") }}</th>
                  <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
                </tr>
              </thead>
              <tbody v-for="(item,index) in dataSearch" :key="item">
                <tr>
                  <td>{{ index + 1 + (searchData.current_page - 1)*10}}</td>
                  <td>{{ item.channel_type }}</td>
                  <td>{{ item.evaluation_name }}</td>
                  <td>{{ item.group_name }}</td>
                  <td>{{ item.job_name }}</td>
                  <td>{{ new Date(item.last_update).toLocaleDateString('en-GB') }}</td>
                  <td>
                    <div class="buttons text-center" style="margin-top: 2px;">
                      <button
                      class="btn btn-primary btn-xs"
                      @click="
                        getDetail(
                          item
                        )
                      "
                    >
                      <Icon style="color: cadetblue" name="edit" />
                      {{ $t('DETAIL') }}
                    </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
            <div class="pagination-search">
              <pagination v-model="searchData.current_page" :records="paginate.totalRecord" :per-page="10" @paginate="myCallback"/>
            </div>
          </fieldset>
          <div v-if="getItemsDetailCheck" class="row p-15"> 
              <fieldset>
                <legend class="configure-elementos">{{ $t('CONFIGURE_ITEMS')}}</legend>
                <fieldset class="m-p-20">
                  <legend> <i class="fa fa-mail-forward"></i> <strong>{{ evaluationName.job_name }}</strong></legend>
                    <div class="row col-md-12">
                      <div class="col-md-3">
                        <p> {{ $t('WEIGHT') }} </p>
                        <p> {{ $t('OPTIONS_OF_ANSWER') }}</p>
                        <p> {{ $t('VALIDATE_OF_EVIDENCE') }} </p>
                        <p> {{ $t('REFERENCIAL_PHOTO') }} </p>
                      </div>
                      <div class="col-md-8">
                        <p> {{ getItemDetail.percent}}% </p>
                        <div class="row col-md-8 m-b-20">
                          <input class="col-md-1 custom" type="checkbox"  v-model="getItemDetail.ok" :value="1" :disabled="!validated"> OK
                          <input class="col-md-1" type="checkbox"  v-model="getItemDetail.nok" @click="changeNok" :value="1" :disabled="!validated"> NOK
                          <input class="col-md-1" type="checkbox"  v-model="getItemDetail.na" :value="1" :disabled="!validated"> NA
                        </div>
                        <div class="row col-md-8 m-b-20">                   
                          <input class="col-md-1" type="checkbox"  v-model="getItemDetail.photo" :value="1" :disabled="!validated" @click="changePhoto"> Photo
                          <input class="col-md-1" type="checkbox"  v-model="getItemDetail.audio" :value="1" :disabled="!validated" @click="changeAudio"> Audio
                          <input class="col-md-1" type="checkbox"  v-model="getItemDetail.file" :value="1" :disabled="!validated" @click="changeFile"> File
                          <input class="col-md-1" type="checkbox"  v-model="getItemDetail.video" :value="1" :disabled="!validated" @click="changeVideo"> Video
                          
                        </div>
                        <p v-if="getItemDetail.url"><button
                            @click="viewImage(getItemDetail.url)"
                            style="
                              padding: 0px;
                              border: none;
                              background: transparent;
                              color: darkcyan;
                              text-decoration-line: underline;
                            "
                          >
                            {{ $t("VIEW_IMAGE") }}
                          </button></p>
                        <p v-if="!getItemDetail.url">{{ $t('NO_FILE') }}</p>
                        <input
                          v-if="validated"
                          id="file"
                          ref="inputFile"
                          accept="image/*,"
                          type="file"
                          @change="previewFiles"
                          />
                      </div>
                      
                    </div>
                    <div v-if="validated && getItemDetail.nok" class="row col-md-12 p-t-20">
                      <div class="col-md-6">
                        <div class="mb-3 row">
                          <label class="col-sm-6 col-form-label"
                            >{{ $t("REASONS_OF_NOK") }}</label
                          >
                          <div class="col-sm-6">
                            <Multiselect
                              v-model="addReason"
                              :options="getListGravedad.getListReason || []"
                              placeholder="Pick a value"
                            ></Multiselect>
                          </div>
                        </div>
                      </div>

                      
                    </div>
                    <div v-if="validated && getItemDetail.nok" class="row col-md-12">
                      <div class="col-md-6">
                        <div class="mb-3 row">
                          <label class="col-sm-6 col-form-label"
                            >{{ $t("GRAVITY") }}</label
                          >
                          <div class="col-sm-6">
                            <Multiselect
                              v-model="addGravedad"
                              :options="getListGravedad.getListGravedad || []"
                              placeholder="Pick a value"
                            ></Multiselect>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div v-if="validated && getItemDetail.nok" class="row col-md-12">
                      <div class="row col-md-6" style="display: flex; justify-content: end;">
                        <button :disabled="!addReason && addGravedad" @click="addList" id="add_btn" type="button" class="btn btn-primary ng-binding" style="width: 30px; height: 30px; font-size: 18px; padding: 0px;"> + </button>
                      </div>
                      
                    </div>
                    <div v-if="getItemDetail.nok" class="item-bot col-md-12" >

                      <div v-if="getItemDetail.reason_list.length" class="col-md-6">
                        <table class="col-md-4 table table-bordered">
                        <thead class="table-success">
                          <tr>
                            <th scope="col">{{ $t("REASONS_OF_NOK") }}</th>
                            <th scope="col">{{ $t("GRAVITY") }}</th>
                            <th  v-if="validated" scope="col">Action</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr v-for="item in getItemDetail.reason_list" :key="item">
                            <td>{{ item.name }}</td>
                            <td>{{ item.gravedad }}</td>
                            <td  v-if="validated">
                              <button @click="showPopupDelete(item, $event)" class="btn btn-danger btn-xs m-r-10"><i class="icofont icofont-ui-delete"></i></button>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                      </div>
                      
                    </div>
                    <div class="row p-t-20">
                      <div class="text-center">
                        <button
                          v-if="!validated"
                          class="col-2 btn btn-danger btn-sm"
                          type="button"
                          @click="editItem()"
                        >
                          {{ $t("EDIT") }}
                        </button>
                        <button
                          v-if="validated"
                          class="col-2 btn btn-danger btn-sm"
                          type="button"
                          @click="saveEdit(getItemDetail)"
                        >
                          {{ $t("SUBMIT") }}
                        </button>
                      </div>
                    </div>
                </fieldset>
              </fieldset>
            </div>
        </div>
    </div>

    <Modal v-model="showPopupViewImg" :title="$t('VIEW_IMAGE')">
      <div class="text-center">
        <img id="mapImg" style="max-width: 100%; padding: 20px;" :src="getContentImg">
      </div>
      <template #modalActions="modalActionProps">
      <button type="button" class="btn btn-primary" @click="downloadFileImage(modalActionProps)">
        {{ $t("DOWNLOAD_LOG") }}
      </button>
    </template>

  </Modal>

  <deleteItemEvaluation v-model="showPopupQuestionDelete" :title="$t('VIEW_IMAGE')">
    <div class="modal-body text-center p-0">
          {{ $t("VISIT_PLAN.LABEL.CONFIRM") }}
    </div>
    <template #modalActions="modalActionProps">
      <button type="button" class="btn btn-danger" @click="deleteResonList(modalActionProps)">
        {{ $t("DELETE") }}
      </button>
    </template>
  </deleteItemEvaluation>

  

  </div>
</template>
<style>
.header-new-page {
  height: 10px;
  margin-bottom: 30px;
}
.d-newpage-none {
  display: none;
}
.multiselect {
  max-height: 35px;
  min-height: 30px;
}
.dp__input_wrap {
  height: 35px;
  display: flex;
}
legend {
  float: initial;
  font-size: 12px !important;
  width: auto;
  border-bottom: none;
  padding: 0px 5px !important;
}
fieldset {
  padding: 18px !important;
  margin: 0 2px;
  border: 1px solid #c0c0c0 !important;
  border-bottom: none !important;
  border-right: none !important;
}
.item-top,.item-bot{
  display: flex;
  padding: 20px 50px;
}

.item-top-left > legend{
  margin-top: 11px;
}

.item-top-right > legend{
  margin-top: 11px;
}

.btn-radio-item label{
  margin-right: 25px;
}

.grave {
  border-collapse: separate; /* Đảm bảo sử dụng border-spacing */
  border-spacing: 0 13px; /* Khoảng cách theo chiều dọc */
}

.suggested-list {
  position: absolute;
  background-color: #fff;
  border: 1px solid #ccc;
  max-height: 150px;
  overflow-y: auto;
  list-style: none;
  padding: 0;
  margin: 0;
  z-index: 100;

}
.item-fill {
    padding: 5px 10px;
    cursor: pointer;
    text-align: justify;
  }
  .item-fill :hover {
      background-color: #f5f5f5;
    }
  .configure-elementos {
    width: auto;
    float: inherit;
    border-bottom: none;
  }

  input[type="checkbox"]:disabled {
    accent-color: #24695c;
  }
  input:disabled {
    background: red;
    color: red;
  }
</style>