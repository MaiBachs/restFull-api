<script setup>
import axios from "axios"
import { ref ,reactive,computed} from "vue"
import Multiselect from "@vueform/multiselect"
import { getName } from "@/store/getName";
import { getCode} from "@/store/getCode";
import { useLoaddingStore } from './../../store/LoaddingStore'
import popupModalError from '@/components/common/popupModalError.vue'
import errorEditEvaluation from '@/components/evaluation/errorEditEvaluation.vue'
import AddChecklist from "@/components/checklist/addChecklist.vue";
import {getParent} from "@/store/getParent";
// import {getSearchParent} from "@/store/getSearchParent";
import ChecklistEdit from '../../components/checklist/ChecklistEdit.vue'
import ChecklistDelete from '../../components/checklist/ChecklistDelete.vue'
// import { notify } from "@kyvg/vue3-notification";
// import index from "vuex";
// import moment from "moment/moment";
const getChecklistDetail = ref({
  job_id: 0
});
// const checkListCL = ref(true);
// function closeModal() {
//   listItems.value = []
//   name.value = null
//   code.value = null
//   job_id.value = null
//   parent_id.value = null
// }
// const listJobId = ref([]);
// const name = ref('');
// const code = ref('');
// const job_id = ref('');
// const parent_id = ref('');
// const listItems = ref([])
const itemEdit = ref({})
const loaddingStore = useLoaddingStore()
const listName = getName();
const listCode = getCode();
// const listSearchParent = getSearchParent();
const listParent = getParent();
// const listChecklist = ref([]);
function myCallback(newPage) {
  searchChecklist(newPage)
}
// const branchEdit = ref('');
// const showSaveButton = ref(false)
const checkDataDetail = ref(false)
// const checkListChecklist = ref(true);

const searchDataChecklist = reactive({
  parent_id: "",
  name: "",
  code: "",
  job_id: "",
  current_page: 0,
  paging: true,
  job_id_lv2: "",
});

const showSaveButton = ref(false)
const getDetail = async (item) => {
  showSaveButton.value = false
  if (item) {
    getChecklistDetail.value = JSON.parse(JSON.stringify(item));
  } else {
    checkDataDetail.value = true
  }
  console.log(getChecklistDetail.value);
}

const getQuestionDelete = async (item) => {
  if (item) {
    try {
    loaddingStore.loading = true;
    const Parent2 = await axios.post("/api/job/fill-check-list-by-parentId", { parent_id: item.job_id});
    if(Parent2.data && Parent2.data.length == 0) {
        getChecklistDetail.value = {
          job_id: item.job_id,
          checkMessage: false
        }
    }else {
      getChecklistDetail.value = {
        job_id: item.job_id,
        checkMessage: true
      }
    }
    loaddingStore.loading = false;
  } catch (error) {
    loaddingStore.loading = false;
  }
  } else {
    checkDataDetail.value = true
  }
};







// async function deleteItemChecklist (item)  {
//   try {
//     loaddingStore.loading = true;
//     const response = await axios.post("/api/job/delete-checklist", { job_id: item.job_id });
//     console.log(response)
//     if(response.status == 200) {
//       notify({
//         type: "success",
//         text: ('SUCCESS_DELETE'),
//         duration: 30000,
//       });
//       searchChecklist();
//       loaddingStore.loading = false;
//     }
//     loaddingStore.loading = true;
//   } catch (error) {
//     loaddingStore.loading = false;
//     notify({
//       type: "error",
//       title: "ERROR DELETE",
//       text: error,
//       duration: 30000,
//     });
//   }
// }

//level1

const actionGetParent = async  (parentId) =>{
  console.log(parentId);
  const Parent = await axios.post('/api/job/fill-check-list-levle-1',
      {
        parent_id: parentId
      });
console.log(actionGetParent);
  if(Parent.data && Parent.data.length) {
    listParent.listParent  = Parent.data.map(function (o) {
      return {
        value: o.job_id,
        label: o.name,
      };
    })
  }
}

//level2
const listParent2 = ref({})
const actionGetParent2 = async  (parentId) =>{
  listParent2.value = []
  console.log(parentId);
  const Parent2 = await axios.post('/api/job/fill-check-list-by-parentId',
      {
        parent_id: parentId
      });
      listParent2.value = []
  if(Parent2.data && Parent2.data.length) {
    listParent2.value  = Parent2.data.map(function (o) {
      return {
        value: o.job_id,
        label: o.name,
      };
    })
  }
}


const getCheckListDetail = ref([]);
const checkListChecklist = ref([])
const listCheckList = ref([]);
const listItemEditId = ref([])
const paginate = reactive ({
  totalPage:"",
  currentPage:"",
  totalRecord: ""
})
const searchChecklist = async () => {
  listItemEditId.value = [];
  getCheckListDetail.value = [];
  listCheckList.value = [];
  checkListChecklist.value = true;
  searchDataChecklist.current_page =  searchDataChecklist.current_page == 0 ? searchDataChecklist.current_page : searchDataChecklist.current_page - 1;
  loaddingStore.loading = true;
  const response  = await axios.post('/api/job/search-checklist', {
        parent_id:  searchDataChecklist.job_id_lv2 ? searchDataChecklist.job_id_lv2 : searchDataChecklist.job_id,
        job_id: searchDataChecklist.job_id,
        job_id_lv2: searchDataChecklist.job_id_lv2,
        name:  searchDataChecklist.name,
        code: searchDataChecklist.code,
        result_data_type: searchDataChecklist.result_data_type,
        parent_name: searchDataChecklist.parent_name,
        current_page: searchDataChecklist.current_page,
        paging: searchDataChecklist.paging,
  });
  if (response && response.data.search_checklist_respones && response.data.search_checklist_respones.length) {
    listCheckList.value = response.data.search_checklist_respones;
    searchDataChecklist.current_page = response.data.current_page + 1;
    paginate.totalPage = response.data.total_page;
    paginate.totalRecord = response.data.total_record;
  } else {
    checkListChecklist.value = false;
  }
  loaddingStore.loading = false;
};

function clearListChecklist() {
  listParent.actionGetParent()
  searchChecklist()
  searchDataChecklist.value = {}
}

//startIndex
const startIndex = computed(() => {
  return (searchDataChecklist.current_page - 1) * 10;
});

const clearLevel2 = () => {
  searchDataChecklist.job_id = null; 
  searchDataChecklist.job_id_lv2 = null;
};

</script>

<template>
  <div class="card-body p-0">
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
          >{{ $t("HOME.SEARCH.MAP.ACTION.STOCK_INFO.NAME") }}:</label
          >
          <div class="col-sm-8">
            <input type="text"
                v-model="searchDataChecklist.name"
                :options="listName.getName || []"
                track-by="name"
                style="min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.375) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 100%;border: var(--ms-border-width,1px) solid var(--ms-border-color,#d1d5db);border-radius: var(--ms-radius,4px); padding-left: 13px;font-size: initial;font-weight: 500;"
            />
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
          >{{ $t("HOME.SEARCH.MAP.ACTION.STOCK_INFO.CODE") }}:</label>
          <div class="col-sm-8">
            <input type="text"
                v-model="searchDataChecklist.code"
                :options="listCode.getCode || []"
                style="min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.375) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 100%;border: var(--ms-border-width,1px) solid var(--ms-border-color,#d1d5db);border-radius: var(--ms-radius,4px); padding-left: 13px;font-size: initial;font-weight: 500;"
            />
          </div>
        </div>
      </div>
    </div><div class="row mb-3">
    <div class="col align-self-start">
      <div class="mb-3 row">
        <label class="col-sm-4 col-form-label">{{ $t("LEVEL_1") }}:</label>
        <div class="col-sm-8">
          <Multiselect
              v-model="searchDataChecklist.job_id"
              :options="listParent.listParent || []"
              :placeholder="$t('SELECT_ONE')" 
              @select="actionGetParent2"
              @clear="clearLevel2"
              ></Multiselect>
        </div>
      </div>
    </div>
    <div class="col align-self-start">
      <div class="mb-3 row" v-if="searchDataChecklist.job_id">
        <label class="col-sm-4 col-form-label ">{{ $t("LEVEL_2") }}:</label>
        <div class="col-sm-8">
          <Multiselect
              v-model="searchDataChecklist.job_id_lv2"
              :options="listParent2 || []"
              :placeholder="$t('SELECT_ONE')"></Multiselect>
        </div>
      </div>
    </div>
  </div>

    <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
            class="btn btn-pill btn-primary-gradien"
            type="button"
            @click="searchChecklist">{{ $t("SEARCH") }}</button>
        <button
            class="btn btn-pill"
            style="background-color: #4682b4; color: #fff"
            type="button"
            data-bs-toggle="modal"
            data-original-title="test"
            data-bs-target="#exampleModaladdChecklist">
          {{ $t("ADD_NEW_CHECKLIST") }}
        </button>
      </div>
    </div>

    <div class="row mb-3 checklists">
      <fieldset v-if="!checkListChecklist" class="table-responsive">
        <div class="text-center no-item">{{$t("NO.ITEM")}}</div>
      </fieldset>
      <fieldset v-if="listCheckList.length" class="table-responsive">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead class="table-success" style="background-color: #24695c; color: #fff;">
            <tr>
              <th scope="col">#</th>
              <th scope="col">{{ $t("PARENT_NAME") }}</th>
              <th scope="col">{{ $t("PARENT_CODE") }}</th>
              <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION.STOCK_INFO.NAME") }}</th>
              <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION.STOCK_INFO.CODE") }}</th>
              <th scope="col" style="text-align: center;">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
            </tr>
            </thead>
            <tbody v-for="(item,index) in listCheckList" :key="item">
            <tr>
              <td>{{ startIndex + index + 1 }}</td>
              <td>{{ item.parent_name }}</td>
              <td>{{ item.parent_code }}</td>
              <td>{{ item.name }}</td>
              <td>{{ item.code }}</td>
              <td>
                <div class="buttons" style="display: flex; flex-direction: row; flex-wrap: nowrap; align-content: center; justify-content: center; align-items: center;">
                  <button
                          class="btn btn-primary btn-xs"
                          data-bs-toggle="modal"
                          data-original-title="test"
                          data-bs-target="#exampleModalEditChecklist"
                          @click="
                        getDetail(
                          item
                        )
                      "
                  >
                  <i class="icofont icofont-edit"></i>
                    <!-- {{ $t('EDIT') }} -->
                  </button>
                  <button style="margin-left: 5px; background-color: rgb(188, 22, 22) !important"
                      class="btn btn-primary btn-xs"
                      data-bs-toggle="modal"
                      data-original-title="test"
                      data-bs-target="#exampleModalDeleteChecklist"
                      @click="getQuestionDelete(item)">
                      <i class="icofont icofont-ui-delete"></i>
                    <!-- nút xoá -->
                  </button>
                  
                </div>
              </td>
            </tr>
            </tbody>
          </table>
          <div class="pagination-search">
              <pagination v-model="searchDataChecklist.current_page" :records="paginate.totalRecord" :per-page="10" @paginate="myCallback"/>
            </div>
        </div>
      </fieldset>
    </div>
      
      
 
<!--    <popupModal modal-id="exampleModal1" :title="$t('ADD_NEW_CHANNEL')">-->
<!--      <addChannel v-model="itemEdit" />-->
<!--      <template>-->
<!--        <button class="btn btn-secondary" type="button" @click="saveaddChannel">Add</button>-->
<!--      </template>-->
<!--    </popupModal>-->


    <AddChecklist @clearValue="clearListChecklist" v-model="itemEdit" :actionGetParent2="actionGetParent2" :actionGetParent="actionGetParent"/>
    <ChecklistEdit v-model="itemEdit" :itemDetail="getChecklistDetail" @clearValue="clearListChecklist" :actionGetParent2="actionGetParent2"/>
    <ChecklistDelete @clearValue="clearListChecklist" v-model="getChecklistDetail" :item="item"/>
    <popupModalError modal-id="popupError" title="Error">
      <errorEditEvaluation />
    </popupModalError>
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
