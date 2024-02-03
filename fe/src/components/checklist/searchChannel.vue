<script setup>
import axios from "axios"
import { ref ,reactive,computed} from "vue"
import Multiselect from "@vueform/multiselect"
// import { getName } from "@/store/getName";
// import { getCode} from "@/store/getCode";
import { useLoaddingStore } from './../../store/LoaddingStore'
// import popupModal from '../../app/popupModal.vue'
import popupModalError from '@/components/common/popupModalError.vue'
import errorEditEvaluation from '@/components/evaluation/errorEditEvaluation.vue'
import ChannelEdit from '../../components/checklist/ChannelEdit.vue'
import ChannelDelete from '../../components/checklist/ChannelDelete.vue'
// import AddChecklist from "@/components/checklist/addChecklist.vue";
import addChannel from "@/components/checklist/addChannel.vue"
// import {getParent} from "@/store/getParent";
// import {getSearchParent} from "@/store/getSearchParent";
// import { notify } from "@kyvg/vue3-notification";
import {getChannelTypeChecklist} from "@/store/getChannelTypeChecklist";
import { i18n } from '../../main.js';
// import index from "vuex";
// import moment from "moment/moment";
// import { getGroups} from './../../store/getGroup'

const itemEdit = ref({})
const loaddingStore = useLoaddingStore()
// const listName = getName();
// const listCode = getCode();
// const listSearchParent = getSearchParent();
// const listParent = getParent();
// const listChecklist = ref([]);
const listChannelTypeChecklist = getChannelTypeChecklist();

const optionsStatus = [
  {
    title:i18n.global.t('ACTIVE'),
    value: 1,
  },
  {
    title: i18n.global.t('INACTIVE'),
    value: 0,
  }
];

// const checkListChecklist = ref(true);

// const searchDataChecklist = reactive({
//   parent_id: "",
//   name: "",
//   code: "",
//   job_id: "",
// });

const paginate = reactive ({
  totalPage:"",
  currentPage:"",
  totalRecord: ""
})

const searchData = reactive({
  channel_type_id: "",
  channel_type_name: "",
  plan_id: "",
  plan_name: "",
  object_type: "",
  object_level: "",
  current_page: 0,
  status:"",
  paging: true
});
const options = [
  {
    title: "SHOP",
    value: "1",
  },
  {
    title: "STAFF",
    value: "2",
  },
];
const optionsobjectlevel = [
  {
    title: "PDV de Bitel (Celular)",
    value: 1,
  },
  {
    title: "PDV de Bitel (solo recarga)",
    value: 2,
  },
  {
    title: "PDV de Bitel (Atencion de Cliente)",
    value: 3,
  },
  {
    title: "PDV del Distribuidor",
    value: 4,
  },
  {
    title: "PDV del Mercado libre",
    value: 5,
  },
  {
    title: "PDV de Bitel (viaje + hotel)",
    value: 6,
  },
];
const showSaveButton = ref(false)
const checkDataDetail = ref(false)
const getDetail = async (item) => {
  
  showSaveButton.value = false
  if (item) {
    getChannelDetail.value = JSON.parse(JSON.stringify(item));
  } else {
    checkDataDetail.value = true
  }
  console.log(item);
};


//them ti xoa
// const checkListCLIST = ref(true);
// const listParent2 = ref({})
// const actionGetParent2 = async  (parentId) =>{
//   const Parent2 = await axios.post('/api/job/fill-check-list-by-parentId',
//       {
//         parent_id: parentId
//       });
//
//   if(Parent2.data && Parent2.data.length) {
//     listParent2.value  = Parent2.data.map(function (o) {
//       return {
//         value: o.job_id,
//         label: o.code,
//       };
//     })
//   }
// }
const getChannelDetail = ref({
  object_level: 0,
  object_type: 0,
  channel_type_name: 0,
  channel_type_id: 0,
  plan_id: 0,
  status: 0
});

const checkListChecklist = ref([])
const listCheckList = ref([]);
// const searchData = reactive({
//   branch_id: "",
//   channel_type_id: "",
//   job_id: "",
//   from_date: "",
//   to_date: "",
// });
const listItemEditId = ref([])
const showObjectLevel = ref(false);
const handleChannelTypeChange = async (selectedItemId) => {
  console.log(searchData.channel_type_id)
  searchData.object_type = "";
  showObjectLevel.value = false;

  const selectedItem = listChannelTypeChecklist.listChannelTypeChecklist.find(item => item.value === selectedItemId);
    if (selectedItem) {
      searchData.object_type = selectedItem.object_type
      console.log(`ID: ${searchData.object_type }, Value: ${searchData.channel_type_id}`);
    }
    console.log(searchData.object_type)

};
const clearChannelType = () => {
  searchData.channel_type_id = null; 
  searchData.object_level = null;
  searchData.object_type = null;
  showObjectLevel.value = false;
};
function myCallback(newPage) {
  searchChecklist(newPage)
}

///dang sua do

const getQuestionDelete = async (item) => {
  console.log(item.plan_id);
  if (item) {
    getChannelDetail.value.plan_id = item.plan_id;
  } else {
    checkDataDetail.value = true
  }
  console.log(getChannelDetail.value);
};


// async function deleteChannel (item)  {
//   try {
//     loaddingStore.loading = true;
//     const response = await axios.post("/api/plan/delete", { plan_id: item.plan_id });
//     if(response.status == 200) {
//       notify({
//         type: "success",
//         text: ('SUCCESS_DELETE'),
//         duration: 10000,
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
//       duration: 10000,
//     });
//   }
// }
const searchChecklist = async () => {
  listItemEditId.value = [];
  getChannelDetail.value = [];
  listCheckList.value = [];
  checkListChecklist.value = true;
  searchData.current_page =  searchData.current_page == 0 ? searchData.current_page : searchData.current_page - 1;
  loaddingStore.loading = true;
  const response = await axios.post(
      'api/plan/search',
      searchData
  );
  if (response && response.data.plan_requests && response.data.plan_requests.length) {
    listCheckList.value = response.data.plan_requests;
    searchData.current_page = response.data.current_page + 1;
    paginate.totalPage = response.data.total_page;
    paginate.totalRecord = response.data.total_record;
  } else {
    checkListChecklist.value = false;
  }
  loaddingStore.loading = false;
};

//Sugget Feature
// const suggestedChannelTypes = ref([]);
// const searchInput = ref("");
// const suggestChannelTypes = () => {
//   if (searchInput.value) {
//     const filteredChannelTypes = listChannelTypeChecklist.listChannelTypeChecklist.filter((channeltype) =>

//         channeltype.value.name.toLowerCase().includes(searchInput.value.toLowerCase())
//     );
//     suggestedChannelTypes.value = filteredChannelTypes.map((channeltype) => channeltype.value);
//   }
//   else {
//     suggestedChannelTypes.value = [];
//   }
// };
// const selectChannelType = (channeltype) => {
//   searchData.channel_type_name = channeltype.name;
//   searchData.channel_type_id = channeltype.id;
//   suggestedChannelTypes.value = [];
// };
// watch(searchData, () => {
//   searchInput.value = searchData.channel_type_name;
//   if(searchInput.value == ""){
//     searchData.channel_type_id = "";
//   }
// });

//search lai
function clearListChannel() {
  searchChecklist()
  searchData.value = {}
}

//startIndex
const startIndex = computed(() => {
  return (searchData.current_page - 1) * 10;
});



</script>

<template>
  <div class="card-body p-0">
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("PLAN_NAME") }}:</label>
          <div class="col-sm-8">
            <input class="input-search-plan"
                v-model="searchData.plan_name"
                style="min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.0) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 300px;border: var(--ms-border-width,1px) solid var(--ms-border-color,#d1d5db);border-radius: var(--ms-radius,4px); padding-left: 26px;font-size: larger;font-weight: 405"
                />
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
          > {{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}:</label>
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.channel_type_id"
              :options="listChannelTypeChecklist.listChannelTypeChecklist || []"
              :placeholder="$t('SELECT_ONE')"
              deselect-label="Can't remove this value"
              track-by="label"
              label="label"
              :searchable="false"
              :allow-empty="false"
              style="width: 100%;" 
              @select="handleChannelTypeChange"
              @clear="clearChannelType"
          ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label">{{ $t("STATUS") }}:</label>
          <div class="col-sm-8">
            <multiselect
                v-model="searchData.status"
                deselect-label="Can't remove this value"
                track-by="title"
                label="title"
                :placeholder="$t('SELECT_ONE')"
                :options="optionsStatus"
                :searchable="false"
                :allow-empty="false">
            </multiselect>
          </div>
        </div>
      </div>
    </div>
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row" v-if="searchData.channel_type_id === 80043">
          <label class="col-sm-4 col-form-label">{{ $t("OBJECT_LEVEL") }}:</label>
          <div class="col-sm-8">
            <multiselect
                v-model="searchData.object_level"
                deselect-label="Can't remove this value"
                track-by="title"
                label="title"
                :placeholder="$t('SELECT_ONE')"
                :options="optionsobjectlevel"
                :searchable="false"
                :allow-empty="false">
            </multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row">
        </div>
      </div>
    </div>

    <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
            class="btn btn-pill btn-primary-gradien"
            type="button"
            @click="searchChecklist"
        >
          {{ $t("SEARCH") }}
        </button>
        <button
        class="btn btn-pill"
            style="background-color: #4682b4; color: #fff"
            type="button"
            data-bs-toggle="modal"
            data-original-title="test"
            data-bs-target="#exampleModal1">{{ $t("ADD_NEW_PLAN") }}
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
              <th scope="col">{{ $t("PLAN_NAME") }}</th>
              <th scope="col">{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}</th>
              <th scope="col">{{ $t("OBJECT_TYPE") }}</th>
              <th scope="col">{{ $t("OBJECT_LEVEL") }}</th>
              <th scope="col">{{ $t("STATUS") }}</th>
              <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
            </tr>
            </thead>
            <tbody v-for="item,index in listCheckList" :key="item">
            <tr>
              <td>{{ startIndex + index + 1 }}</td>
              <td >{{ item.plan_name }}</td>
              <td>{{ item.channel_type_name }}</td>
              <td>{{ item.object_type ==1 ? "SHOP" : "STAFF" }}</td>
              <td>{{ item.object_level_name }}</td>
              <td v-if="item.status == 1">{{ $t('ACTIVE') }}</td>
              <td v-if="item.status != 1">{{ $t('INACTIVE') }}</td>
              <td >
                <div class="buttons">
                  <button
                      class="btn btn-primary btn-xs"
                      data-bs-toggle="modal"
                      data-original-title="test"
                      data-bs-target="#exampleModal"
                      
                      @click="
                        getDetail(
                          item)">
                    <i class="icofont icofont-edit"></i>
                    <!-- nút edit -->
                  </button>
                  <button style="margin-left: 5px;background-color: rgb(188, 22, 22) !important"
                          class="btn btn-primary btn-xs"
                          data-bs-toggle="modal"
                          data-original-title="test"
                          v-if="item.status === 1"
                          data-bs-target="#exampleModalDelete"
                          @click="getQuestionDelete(item) ">
                    <i class="icofont icofont-ui-delete"></i>
                    <!-- nút xoá -->
                  </button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
          <div class="pagination-search">
            <pagination v-model="searchData.current_page" :records="paginate.totalRecord" :per-page="10" @paginate="myCallback"/>
          </div>
        </div>
      </fieldset>
    </div>

    <addChannel  @clearValue="clearListChannel" v-model="itemEdit" />
    <ChannelEdit v-model="getChannelDetail" :optionsobjectlevel="optionsobjectlevel" :options="options" @clearValue="clearListChannel" />
    <ChannelDelete @clearValue="clearListChannel" v-model="getChannelDetail" :item="item" />
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
.input-search-plan {
  width: 320px;
  border: 1px solid #cdcdcd;
  height: 30px;

}
</style>
