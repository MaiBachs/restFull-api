<script setup>
// import {defineEmits, defineProps, computed, reactive} from "vue"
import Multiselect from "@vueform/multiselect"
import {reactive} from "vue"
import { notify } from "@kyvg/vue3-notification";
import {getChannelTypeChecklist} from "@/store/getChannelTypeChecklist";
import { ref, defineEmits} from "vue";
import popupModal from '../../app/popupModal.vue'
import axios from "axios";
import { useLoaddingStore } from './../../store/LoaddingStore';
import { getChannelType} from "@/store/getChannelType";

const loaddingStore = useLoaddingStore()
// const emit = defineEmits(['onOk', 'update:modelValue'])
// const checkListCL = ref(true);
const getlistPlan = getChannelType()
const listChannelTypeChecklist = getChannelTypeChecklist();
const emit = defineEmits(['update:modelValue'])
const searchData = reactive({
  channel_type_id: "",
  plan_name: "",
  object_type: "",
  object_level: "",
  frequency: "",
  frequency_unit: "",
});
// const options = [
//   {
//     title: "SHOP",
//     value: "1",
//   },
//   {
//     title: "STAFF",
//     value: "2",
//   },
// ];
const optionsobjectlevel = [
  {
    title: "PDV de Bitel (Celular)",
    value: "1",
  },
  {
    title: "PDV de Bitel (solo recarga)",
    value: "2",
  },
  {
    title: "PDV de Bitel (Atencion de Cliente)",
    value: "3",
  },
  {
    title: "PDV del Distribuidor",
    value: "4",
  },
  {
    title: "PDV del Mercado libre",
    value: "5",
  },
  {
    title: "PDV de Bitel (viaje + hotel)",
    value: "6",
  },
];

const saveaddChannel = async (action) =>{
  loaddingStore.loading = true;
  const response  = await axios.post('/api/plan/add', {
    channel_type_id: searchData.channel_type_id,
    plan_name:  searchData.plan_name,
    object_type: searchData.object_type,
    object_level: searchData.object_level,
    frequency: searchData.frequency,
    frequency_unit: searchData.frequency_unit,
  });
  console.log(response)
  if(response && response.data.status.message && response.data.status.message.length > 0) {
    notify({
      type: 'error',
      title: "Add Error",
      text: response.data.status.message,
      duration: 10000,
    });
    loaddingStore.loading = false;
  } else {
    notify({
      type: 'success',
      title: "Add successful",
      // text: response.data,
      duration: 10000,
      
    });
    getlistPlan.actionGetListFillPlan()
    action.onClose();
    emit('clearValue') //clear thong tin va search lai
  }
};

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
  searchData.object_type = null;
  searchData.object_level = null;
  showObjectLevel.value = false;
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
function closeModal() {
  searchData.value = []
  searchData.channel_type_name = null
  searchData.object_type = null
  searchData.object_level = null
  searchData.plan_name = null
  searchData.channel_type_id = null
}
</script>

<template>
  <popupModal modal-id="exampleModal1" :title="$t('ADD_NEW_PLAN')" @close-modal="closeModal">
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center" style="padding-right: 30px">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("PLAN_NAME") }}:</label>
          <input
              v-model="searchData.plan_name"
              style="min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.375) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 300px;border: var(--ms-border-width,1px) solid var(--ms-border-color,#d1d5db);border-radius: var(--ms-radius,4px); padding-left: 26px;font-size: larger;font-weight: 405;"/>
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}:</label>
          <Multiselect
              v-model="searchData.channel_type_id"
              :options="listChannelTypeChecklist.listChannelTypeChecklist || []"
              :placeholder="$t('SELECT_ONE')"
              deselect-label="Can't remove this value"
              track-by="label"
              label="label"
              :searchable="false"
              :allow-empty="false"
              @select="handleChannelTypeChange(searchData.channel_type_id)"
              @clear="clearChannelType"
              style="width: 300px;"
          ></Multiselect>
        </div>
      </div>

    </div>
    <div class="row col-md-12 p-b-15">
   <!--   <div class="col-md-6 text-center" style="padding-right: 39px">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("OBJECT_TYPE") }}:</label>
          <multiselect
              v-model="searchData.object_type"
              deselect-label="Can't remove this value"
              track-by="title"
              label="title"
              :placeholder="$t('SELECT_ONE')"
              :options="options"
              :searchable="false"
              :allow-empty="false"
              style="width: 300px;">
          </multiselect>
        </div>
      </div> -->
      <div class="col-md-6 text-center" v-if="searchData.channel_type_id === 80043" style="padding-right: 39px">
        <div class="row" >
          <label class="col-md-5 form-label">{{ $t("OBJECT_LEVEL") }}:</label>
          <multiselect
              v-model="searchData.object_level"
              deselect-label="Can't remove this value"
              track-by="title"
              label="title"
              :placeholder="$t('SELECT_ONE')"
              :options="optionsobjectlevel"
              :searchable="false"
              :allow-empty="false"
              style="width: 300px;">
          </multiselect>
        </div>
      </div>
      <div class="col-md-6 text-center">
      </div>
      <div class="col-md-6 text-center">
      </div>
    </div>
    <template #modalActions="modalActionProps">
      <button :disabled="!searchData.plan_name || !searchData.channel_type_id || (searchData.channel_type_id === 80043 && !searchData.object_level)" type="submit" class="btn btn-primary ng-binding" style="width: 175px; text-align: center; " @click="saveaddChannel(modalActionProps)">{{ $t("ADD") }} </button>
    </template>
  </popupModal>

</template>
<style scoped>
.form-label {
  text-align: start;
}
.form-control-input {
  height: 30px;
  border: 1px solid #dee2e6;
  border-radius: 6px;
}
.form-control-date {
  width: 58%;
  padding: 0px;
  height: 35px;
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
</style>
