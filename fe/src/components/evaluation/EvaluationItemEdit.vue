<script setup>
import {  defineEmits, defineProps, computed } from "vue"
import VueDatePicker from "@vuepic/vue-datepicker";
const emit = defineEmits(['onOk', 'update:modelValue'])
const props = defineProps({
  modelValue: {
    type: Object,
    required: true,
    default: () => ({})
  }
})

const editItem = computed({
  get() {
    return props.modelValue || {}
  },
  set(value) {
    emit('update:modelValue', value)
  }
})
</script>

<template>
  <div class="row col-md-12 p-b-15">
    <div class="col-md-6 text-center">
      <div class="row">
        <label class="col-md-5 form-label"
          >{{ $t("QUANTITY_PER_MONTH") }}:</label
        >
        <input
          v-model="editItem.quantity_per_month"
          class="form-control-input col-md-7"
          type="number"
          min="1"
          max="4"
        />
      </div>
    </div>
    <div class="col-md-6 text-center">
      <div class="row">
        <label class="col-md-5 form-label">{{ $t("APPROVAL_SCORE") }}:</label>
        <input
          v-model="editItem.approval_score"
          class="form-control-input col-md-7"
          type="number"
          min="1"
          max="100"
        />
      </div>
    </div>
  </div>
  <div class="row col-md-12 p-b-15">
    <div v-if="editItem.quantity_per_month >= 1" class="col-md-6 text-center">
      <div class="row">
        <label class="col-md-5 form-label"
          >{{ $t("DATE_OF_EVALUTION") }} 1:</label
        >
        <VueDatePicker
          v-model="editItem.date_evaluation1"
          class="col-md-7 form-control-date"
          auto-apply
          format="dd-MM-yyyy"
        ></VueDatePicker>
      </div>
    </div>
    <div v-if="editItem.quantity_per_month >= 2" class="col-md-6 text-center">
      <div class="row">
        <label class="col-md-5 form-label"
          >{{ $t("DATE_OF_EVALUTION") }} 2:</label
        >
        <VueDatePicker
          v-model="editItem.date_evaluation2"
          class="col-md-7 form-control-date"
          auto-apply
          format="dd-MM-yyyy"
        ></VueDatePicker>
      </div>
    </div>
  </div>
  <div class="row col-md-12 p-b-15">
    <div v-if="editItem.quantity_per_month >= 3" class="col-md-6 text-center">
      <div class="row">
        <label class="col-md-5 form-label"
          >{{ $t("DATE_OF_EVALUTION") }} 3:</label
        >
        <VueDatePicker
          v-model="editItem.date_evaluation3"
          class="col-md-7 form-control-date"
          auto-apply
          format="dd-MM-yyyy"
        ></VueDatePicker>
      </div>
    </div>
    <div v-if="editItem.quantity_per_month >= 4" class="col-md-6 text-center">
      <div class="row">
        <label class="col-md-5 form-label"
          >{{ $t("DATE_OF_EVALUTION") }} 4:</label
        >
        <VueDatePicker
          v-model="editItem.date_evaluation4"
          class="col-md-7 form-control-date"
          auto-apply
          format="dd-MM-yyyy"
        ></VueDatePicker>
      </div>
    </div>
  </div>
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
