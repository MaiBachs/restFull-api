<script setup>
import {  defineEmits, defineProps, computed, reactive } from "vue"
import { ref } from 'vue';
import { i18n } from '../../main.js';
import { notify } from "@kyvg/vue3-notification";
import popupModal from '../../app/popupModal.vue'

const emit = defineEmits(['onOk', 'update:modelValue'])
const props = defineProps({
  modelValue: {
    type: Object,
    required: true,
    default: () => ({})
  },
  listItem: {
    type: Object,
    required: true,
    default: () => ({})
  }
})
const updateData = reactive({
  channel_code_id: "",
  object_type: "",
  latitude: "",
  longitude:"",
  radius:"",
})




const editItem = computed({
  get() {
    return props.modelValue || {}
  },
  set(value) {
    emit('update:modelValue', value)
  }
})
const canOk = computed(() => JSON.stringify(props.listItem) !== JSON.stringify(editItem.value))


const longitude = ref(0);
const latitude = ref(0);
const longitudeError = ref(false);
const latitudeError = ref(false);
const errors = ref([]);
const onCustomStyleSubmit = () => {
  console.log(editItem);
  longitudeError.value = false;
  latitudeError.value = false;
  if (longitude.value < -180 || longitude.value >= 180) {
    longitudeError.value = true;
    errors.value.push({
      'message5': i18n.t("ERROR_LONGTITUDE"),
    });
  }

  if (latitude.value < -90 || latitude.value >= 90) {
    latitudeError.value = true;
    errors.value.push({
      'message6': i18n.t("ERROR_LATITUDE"),
    });
  }
}
const formSubmitted = ref(false);
// const submitForm = () => {
//   onCustomStyleSubmit();
//   formSubmitted.value = true;
//   // Kiểm tra nếu có lỗi thì hiển thị thông báo
//   if (errors.value.length > 0) {
//     // Hiển thị lỗi ở đây, ví dụ:
//     errors.value.forEach((error) => {
//       console.error(error);
//     });
//   } else {
//     // Nếu không có lỗi, tiến hành submit form hoặc thực hiện các tác vụ khác ở đây
//   }
// };
const loaddingStore = useLoaddingStore()
import axios from "axios";
import { useLoaddingStore } from "../../store/LoaddingStore";
async function updateLocation(actions) {
  const longitude = parseFloat(editItem.value.longitude);
  const latitude = parseFloat(editItem.value.latitude);
  const radius = parseFloat(editItem.value.radius);
  if(!isNaN(radius) && radius <= 0 ){
    notify({
      type: "error",
      title: "Invalid Coordinates",
      text:  i18n.global.t("ONLY_POSITIVE_NUMBER"),
      duration: 10000,
    });
  } else if(!isNaN(longitude) && (longitude < -180 || longitude >= 180)) {
    notify({
      type: "error",
      title: "Invalid Coordinates",
      text:  i18n.global.t("LATITUDE_IS_SPECIFIED"),
      duration: 10000,
    });
  } else if(!isNaN(latitude) && (latitude < -90 || latitude > 90)) {
    notify({
      type: "error",
      title: "Invalid Coordinates",
      text:  i18n.global.t("LAT_RANGE_90"),
      duration: 10000,
    });
  }else {
    if (editItem.value.id != null) {
      if(editItem.value) {
        if(editItem.value.object_type == "1") {
          updateData.object_type = 1
        }else if(editItem.value.object_type == "2") {
          updateData.object_type = 2
        }
        updateData.channel_code_id = editItem.value.id
        updateData.latitude = editItem.value.latitude
        updateData.longitude = editItem.value.longitude
        updateData.radius = editItem.value.radius
        try {
          loaddingStore.loading = true;
          const response = await axios.post("/api/Location-manager/update-location", updateData);
          console.log(response.data)
          loaddingStore.loading = false;
          if(response.data == true){
            notify({
              type: "success",
              title: i18n.global.t('EDIT_SUCCESSS'),
              text: i18n.global.t('EDIT_SUCCESSS') + " " + editItem.value.code,
              duration: 10000,
            });
            actions.onClose()
            emit('clearValue')
          }else{
            notify({
              type: "error",
              title: i18n.global.t('EDIT_ERROR'),
              text: i18n.global.t('EDIT_ERROR') + " " + editItem.value.code,
              duration: 10000,
            });
          }
        } catch (error) {
          loaddingStore.loading = false;
          notify({
            type: "error",
            title: "Get current user",
            text: error,
            duration: 10000,
          });
        } 
      }
      
    }
  }

}
</script>

<template>
  <popupModal modal-id="exampleModal" :title="$t('UPDATE_LOCATION')+':' + ' ' + editItem.code || ''" >
    <form @submit.prevent="onCustomStyleSubmit">
      <div class="row col-md-12 p-b-15" >
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label"
            >{{ $t("LOCATION.LATIT") }}:</label
          >
          <input
            v-model="editItem.latitude"
            class="form-control-input col-md-7"
            v-bind:class="formSubmitted ? (latitudeError ? 'is-invalid' : 'is-valid') : ''"
            type="number"
            min="-89.9999999999" 
            max="89.9999999999" 
            title="Value must be between -90 and 90"
          />
          <div class="invalid-tooltip" v-if="errors.some(error => error.message6)">{{ errors.find(error => error.message6) }}</div>
        </div>
        
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label"
            >{{ $t("LOCATION.LONGIT") }} :</label
          >
          <input
            v-model="editItem.longitude"
            class="form-control-input col-md-7"
            v-bind:class="formSubmitted ? (longitudeError ? 'is-invalid' : 'is-valid') : ''"
            type="number"
            min="-179.9999999999"
            max="179.9999999999"
            title="Value must be between -180 and 179.99"
          />
          <div class="invalid-tooltip" v-if="errors.some(error => error.message5)">{{ errors.find(error => error.message5) }}</div>
        </div>
      </div>
      </div>
      <div class="row col-md-12 p-b-15">
        <div class="col-md-6 text-center">
          <div class="row">
            <label class="col-md-5 form-label"
              >{{ $t("LOCATION.RADIUS") }}:</label
            >
            <input
              v-model="editItem.radius"
              class="form-control-input col-md-7"
              type="number"
              step="0.01" 
              min="1"
              max="10"
            />
          </div>
        </div>
      </div>
    </form>
    <template #modalActions="modalActionProps">
        <button v-if="canOk" class="btn btn-primary" type="button" @click="updateLocation(modalActionProps)">
          {{ $t("SUBMIT") }}
        </button>
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
</style>
