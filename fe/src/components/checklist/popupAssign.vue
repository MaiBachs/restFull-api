<script setup>
import Multiselect from "@vueform/multiselect"
// import Autocomplete from 'vue3-autocomplete';
import axios from "axios"
import { i18n } from '../../main.js';
import { useLoaddingStore } from "./../../store/LoaddingStore";
import popupModal from '../../app/popupModal.vue'
const loaddingStore = useLoaddingStore();
import { notify } from "@kyvg/vue3-notification";
import { getChannelType } from "@/store/getChannelType";
import useAssign from "@/composables/useCheckListAssign"
const getListLevel = getChannelType();
import { ref, reactive, defineProps, defineEmits} from "vue"
const { editAssignQueue, assignQueue,valueEditAssignQueue,editValueAssignQueue} = useAssign(ref)
const idxLevel1 = ref("")
const idxLevel2 = ref("")
const props = defineProps({
  modelValue: {
    type: Array,
    required: true,
    default: () => []
  },
  item: {
    type: String,
    required: true,
  },
});
const emit = defineEmits(['update:modelValue'])
const searchData = reactive({
    parent_id: "",
});
const searchDataLevel2 = reactive({
    parent_id: "",
    plan_id:""
});
const itemAddAssignLevel1 = reactive({
  job_id: "",
  idx: '',
  plan_id: false,
});
const itemAddAssignLevel2 = reactive({
  job_id: "",
  idx: '',
  plan_id: false,
});
const listFitterLevel3 = ref([])
const level1 = ref('');
const level2 = ref('');
const level3 = ref('');
const getListLevel2 = ref([])
const getListLevel3 = ref([])
const listItems = ref([])
const listItemsEdit = ref([])

const changeGetListLevel = async () => {
  listItems.value = [];
  level2.value = {}
  console.log(level1.value)
  searchData.parent_id = level1.value
    try {
        loaddingStore.loading = true;
        const response = await axios.post(
          "/api/job/fill-check-list-by-parentId",
          searchData
        );
        if(response.data && response.data.length) {
            getListLevel2.value =  response.data.map(function (o) {
                return {
                value: o.job_id,
                label: o.name +'('+ o.code +')',
                };
            })
            
        }else{
            getListLevel2.value = []
        }
        loaddingStore.loading = false;
      } catch (error) {
        loaddingStore.loading = false;
        notify({
          type: "error",
          title: "Get current user",
          text: error,
          duration: 30000,
        });
      }
}
const changeGetListLevel2 = async (item2) => {
  listItems.value = [];
  emit('update:modelValue', [])
  searchDataLevel2.parent_id = item2
  searchDataLevel2.plan_id =  props.item
    try {
        loaddingStore.loading = true;
        const response = await axios.post(
          "api/job/fill-check-list-lv3-not-assign-by-parentId",
          searchDataLevel2
        );

        if(response.data && response.data.length) {
          listItems.value = response.data.map(function (o) {
            return {
              job_id: o.job_id,
              name: o.name,
              code: o.code,
              plan_id: false,
              require: false,
              required_file: false,
              result_data_type:o.result_data_type,
              idx: "",
            }
          });
          getListLevel3.value =  response.data.map(function (o) {
              return {
                value: o.job_id,
                label: o.name +'('+ o.code +')',
              };
              
          })
          listFitterLevel3.value = listItems.value ;
        }else{
            getListLevel3.value = []
        }

        loaddingStore.loading = false;
    } catch (error) {
      loaddingStore.loading = false;
      notify({
        type: "error",
        title: "Get current user",
        text: error,
        duration: 100000,
      });
    }
}
const changeGetListLevel3 = async (item3) => {
  loaddingStore.loading = true;
  listItems.value = []
  listItems.value = listFitterLevel3.value.filter(o => (item3 == o.job_id))
  loaddingStore.loading = false;
}
const updateRowData = (data) => {
  if(data.idx) {
    if (!/^[0-9]\d*$/.test(data.idx)) {
      notify({
        type: "error",
        title: "Get current user",
        text: i18n.global.t('PLEASE_ENTER_POSITIVE'),
        duration: 10000,
      });
      data.idx = ""
      return;
    }
  }else {
    data.idx = ""
  }
  if(data.required || data.required_file || data.idx) {
      editAssignQueue({...data})
  }
}
function closeModal() {
  listItems.value = []
  level1.value = null
  level2.value = null
  level3.value = null
  idxLevel1.value = null
  idxLevel2.value = null
  assignQueue.value = []
  listFitterLevel3.value = []
  getListLevel3.value = []
  getListLevel2.value = []
  editValueAssignQueue.value = []
}

const updateItemToQueue = async (actions) => {
  var shouldContinueProcessing = true;
  // if(assignQueue.value.length && !idxLevel1.value || assignQueue.value.length && !idxLevel2.value) {
  //   notify({
  //         type: "error",
  //         title: "Get current user",
  //         text: i18n.global.t('REQUIRED_LEVEL_1_2'),
  //         duration: 30000,
  //       });
  //       return;
  // } else if(assignQueue.value.length == 0 && idxLevel2.value && idxLevel2.value) {
  //   notify({
  //         type: "error",
  //         title: "Get current user",
  //         text: i18n.global.t('REQUIRE_PLEASE_ADD_ITEM_LEVEL'),
  //         duration: 30000,
  //       });
  //       return;
  // }else {
  //   if(level1.value) {
  //     itemAddAssignLevel1.job_id = level1.value
  //     itemAddAssignLevel1.plan_id = props.item
  //     itemAddAssignLevel1.idx = idxLevel1.value
  //     assignQueue.value.push(itemAddAssignLevel1)
  //   }
  //   if(level2.value) {
  //     itemAddAssignLevel2.job_id = level2.value
  //     itemAddAssignLevel2.plan_id = props.item
  //     itemAddAssignLevel2.idx = idxLevel2.value
  //     assignQueue.value.push(itemAddAssignLevel2)
  //   }
  //     assignQueue.value = assignQueue.value.map(function (o) {
  //       return {
  //         job_id: o.job_id,
  //         plan_id: props.item,
  //         required:  o.required === true ? 1 : 0,
  //         required_file: o.required_file === true ? 1 : 0,
  //         idx: o.idx,
  //       }
  // });

  // if(assignQueue.value && assignQueue.value.length) {
  //   if(checkValueIndex.value) {
  //     notify({
  //       type: "error",
  //       title: "Get current user",
  //       text: i18n.global.t('NOT_INDEX'),
  //       duration: 50000,
  //     });
  //     return
  //   } else if(checkValueIndexInteger.value) {
  //     notify({
  //       type: "error",
  //       title: "Get current user",
  //       text: i18n.global.t('PLEASE_ENTER_POSITIVE'),
  //       duration: 50000,
  //     });
  //     return 
  //   }
      
  // }else {
  //   notify({
  //     type: "error",
  //     title: "Get current user",
  //     text: i18n.global.t('NOT_INDEX'),
  //     duration: 50000,
  //   });
  // }
  // }

  if(level1.value) {
      itemAddAssignLevel1.job_id = level1.value
      itemAddAssignLevel1.plan_id = props.item
      itemAddAssignLevel1.idx = idxLevel1.value
      assignQueue.value.push(itemAddAssignLevel1)
    }
    if(level2.value) {
      itemAddAssignLevel2.job_id = level2.value
      itemAddAssignLevel2.plan_id = props.item
      itemAddAssignLevel2.idx = idxLevel2.value
      assignQueue.value.push(itemAddAssignLevel2)
    }

  let value = []
  editValueAssignQueue.value = []
  assignQueue.value.forEach(function (o) {
  if (o.idx == "" || !/^[0-9]\d*$/.test(o.idx)) {
    return shouldContinueProcessing = false;
  }
  value = {
    job_id: o.job_id,
    plan_id: props.item,
    required:  o.required === true ? 1 : 0,
    required_file: o.required_file === true ? 1 : 0,
    idx: o.idx,
  };

  valueEditAssignQueue({...value})
});
  if(shouldContinueProcessing) {
    try {
        loaddingStore.loading = true;
        const response = await axios.post(
          "/api/plan-job/new-assign-checklist",
          editValueAssignQueue.value
          );
          if(response.status.message) {
            notify({
              type: "error",
              title: "Get current user",
              text: response.status.message,
              duration: 50000,
            });
          }
          else if(response.data && response.data.length) {
            
            emit('update:modelValue', assignQueue.value) 
            listItemsEdit.value = []
            actions.onClose()// update model ==> data thay dooi 
            emit('clearValue')
          } 
      } catch (error) {
        loaddingStore.loading = false;
        notify({
            type: "error",
            title: "Get current user",
            text: error,
            duration: 50000,
          });
      }
  }else {
    notify({
        type: "error",
        text: i18n.global.t('NOT_INDEX'),
        duration: 30000,
      });
      return
  }
}

function clearModel() {
  editValueAssignQueue.value = []
  listItems.value = listFitterLevel3.value
  assignQueue.value = []
}

const getLableLevel1 = (value) => {
  let label = "";
  if(value) {
    label = getListLevel.getListCheckListLevel1.filter(o => (value == o.value))
    if(label) {
      label = label[0].label
    } else {
      label = ""
    }
  } else {
    label = ""
  }
  return label
  
}
const getLableLevel2 = (value) => {
  let label = "";
  if(value) {
    label = getListLevel2.value.filter(o => (value == o.value))
    if(label) {
      label = label[0].label
    } else {
      label = ""
    }
  } else {
    label = ""
  }
  return label
  
}
const updateRowDataIndexLevel1 = () => {
  if( idxLevel1.value == "") {
    idxLevel1.value = ""
    notify({
      type: "error",
      title: "Get current user",
      text: i18n.global.t('PLEASE_ENTER_NUMBER_ONLY'),
      duration: 10000,
    });
    return;
  }
  else if (!/^[0-9]\d*$/.test(idxLevel1.value)) {
    idxLevel1.value = ""
      notify({
        type: "error",
        title: "Get current user",
        text: i18n.global.t('PLEASE_ENTER_POSITIVE'),
        duration: 10000,
      });
      return;
    }
  
}
const updateRowDataIndexLevel2 = () => {
  if(idxLevel2.value == "") {
    idxLevel2.value = ""
    notify({
      type: "error",
      title: "Get current user",
      text: i18n.global.t('PLEASE_ENTER_NUMBER_ONLY'),
      duration: 10000,
    });
    return;
  }
  else if (!/^[0-9]\d*$/.test(idxLevel2.value)) {
      idxLevel2.value = ""
      notify({
        type: "error",
        title: "Get current user",
        text: i18n.global.t('PLEASE_ENTER_POSITIVE'),
        duration: 10000,
      });
      return;
    }
  
}
</script>
<template>
  <popupModal modal-id="popupShowAssign" title="Assiginment" @close-modal="closeModal">
    <div class="card-body p-0">
      <div class="row mb-3">
        <div class="col align-self-start">
          <div class="mb-3 row">
            <label class="col-sm-4 col-form-label"
              >{{ $t('LEVEL_1') }} <span class="require">*</span>:</label
            >
            <div class="col-sm-8">

              <Multiselect
                v-model="level1"
                :options="getListLevel.getListCheckListLevel1 || []"
                track-by="label"
                @Select ="changeGetListLevel"
                :placeholder="$t('SELECT_ONE')"
              ></Multiselect>
            </div>
          </div>
        </div>
        <div class="col align-self-start">
          <div class="mb-3 row">
              <label class="col-sm-4 col-form-label"
              > {{ $t('LEVEL_2') }} <span class="require">*</span>:</label
            >
            <div class="col-sm-8">
              <Multiselect
                v-model="level2"
                :options="getListLevel2 || []"
                @Select="changeGetListLevel2"
                track-by="label"
                :placeholder="$t('SELECT_ONE')"
              ></Multiselect>
          </div>
        </div>
      </div>
      </div>
      <div class="row mb-3">
        <div class="col align-self-start">
          <div class="mb-3 row">
            <label class="col-sm-4 col-form-label"
              >{{ $t('LEVEL_3') }} :</label
            >
            <div class="col-sm-8">
              <Multiselect
                v-model="level3"
                :options="getListLevel3"
                track-by="label"
                @select="changeGetListLevel3"
                @clear="clearModel"
                :placeholder="$t('SELECT_ONE')"
              ></Multiselect>
            </div>
          </div>
        </div>
        <div class="col align-self-start">
        
      </div>
      
      </div>

      <div v-if="listItems?.length && level1 && level2" class="row p-15">
        <table class="table table-bordered table-hover">
          <thead class="table-success">
          <tr>
            <th scope="col">{{$t("MENU/SUBMENU")}}</th>
            <th scope="col">{{$t("REQUIRE")}}</th>
            <th scope="col">{{$t("FILE_REQUIRE")}}</th>
            <th scope="col">{{ $t('FILE_TYPE') }}</th>
            <th scope="col">Index<span class="lable-require">*</span></th>

          </tr>
          </thead>
          <tbody>
            <tr>
              <td class="list_parent1">{{ getLableLevel1(level1) }}</td>
              <td></td>
              <td></td>
              <td></td>
              <td class="index-input">
                <input @change="updateRowDataIndexLevel1" v-model="idxLevel1" type="text">

              </td>
            </tr>
            <tr>
              <td class="list_parent2">{{ getLableLevel2(level2) }}</td>
              <td></td>
              <td></td>
              <td></td>
              <td class="index-input">
                <input @change="updateRowDataIndexLevel2" v-model="idxLevel2" type="text">
              </td>
            </tr>
            <tr v-for="data in listItems" :key="data" class="editable-row">
              <td class="list_parent3">
                <p style="margin-bottom: 0px;"> {{ data.name }}</p>
                <p style="font-size: 13px;color: darkviolet;"> ({{ data.code }})</p>
                </td>
              <td><input type="checkbox" v-model="data.required" @change="updateRowData(data)"></td>
              <td><input type="checkbox" v-model="data.required_file" @change="updateRowData(data)"></td>
              <td>
                <p v-if="data.result_data_type == 1">Photo</p>
                <p v-if="data.result_data_type == 0"></p>
              </td>
              <td class="index-input">
                <input @change="updateRowData(data)" v-model="data.idx" type="text">
              </td>
            
            </tr>
          </tbody>
        </table>
      </div>

    </div>
    <template #modalActions="modalActionProps">
      <button v-if="assignQueue.length" class="btn btn-primary" type="button" @click="updateItemToQueue(modalActionProps)">
        {{ $t('SUBMIT') }}
      </button>
    </template>
  </popupModal>
</template>
<style scoped>
.index-input input {
    border: 1px solid #cdcdcd;
    border-radius: 5px;

}
.require {
  color: red;

}
</style>
