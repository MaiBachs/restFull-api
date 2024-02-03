<script setup>
// import {computed, defineProps, ref, defineEmits,reactive} from "vue";
// import useChannelManager from "@/composables/useChannelManager"
import popupMonth from "@/components/channelManagerBranch/widgets/popup-month.vue";

import { defineProps, ref , reactive} from "vue";

const props = defineProps({
  listChannelLevel3: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  searchData: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});

const listMapPlan = reactive({
    channel_type_id: "",
    staff_code: "",
    from_date: "",
    target_id:"",
    current_page: 1
});

// const itemDetail = computed({
//   get() {
//     return props.searchData;
//   },
//   set(value) {
//     emit('update:modelValue', value)
//   }
// },);
// const emit = defineEmits(['onOk', 'update:itemDetail'])

// // const showPopupViewLevel3 = ref(false)
// // const  { searchChannel} = useChannelManager(ref);
const showPopupMonth = ref(false);
function actionListMapPlan(channel_id, staff_code,target_id) {
  showPopupMonth.value = true;
  listMapPlan.channel_type_id = channel_id
  listMapPlan.staff_code = staff_code
  listMapPlan.target_id = target_id
}
</script>
<template>
  <div class="table-responsive">
    <table class="table table-bordered table-hover">
      <thead
        class="table-success"
        style="background-color: #24695c; color: #fff"
      >
        <tr class="black-muted-bg" style="background: #209e91; color: #fff">
          <th rowspan="2" class="text-center" style="min-width: 150px">
            {{ $t("HOME.SEARCH.COMBOBOX.ZONAL") }}
          </th>
          <th rowspan="2" class="text-center" style="min-width: 320px">
            {{ $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.JANUARY") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.FEBRUARY") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.MARCH") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.APRIL") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.MAY") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.JUNE") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.JULY") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.AUGUST") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.SEPTEMBER") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.OCTOBER") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.NOVEMBER") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("DATE.MONTHNAMES.DECEMBER") }}
          </th>
          <th colspan="3" class="text-center">
            {{ $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.TOTAL") }}
          </th>
          <th rowspan="2" class="text-center" style="line-height: 4">
            {{ $t("HOME.SEARCH.MAP.ACTION") }}
          </th>
          <!-- <th colspan="1" class="text-center"></th> -->
        </tr>
        <tr class="no-top-border target" style="border-bottom: 1px solid #ddd">
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <td class="text-center text-target">T</td>
          <td class="text-center text-danger">P</td>
          <td class="text-center text-result">R</td>
          <!-- <td class="text-center">{{'HOME.SEARCH.MAP.ACTION' | translate}}</td> -->
        </tr>
      </thead>
      <tbody v-for="item in props.listChannelLevel3" :key="item">
        <tr
          class="no-top-border"
          style="border-bottom: 1px solid #ddd"
          :class="{ 'total-row': item.staff_code === 'Total' }"
        >
          <td
            :rowspan="item.channels.length + 1"
            class="text-left"
            style="vertical-align: middle; text-align: center"
          >
            {{ item.staff_code }}
          </td>
        </tr>
        <tr
          v-for="channel in item.channels"
          :key="channel"
          :class="{
            'text-color-target': channel.target_type == 2,
            'total-row': item.staff_code === 'Total',
          }"
          class="no-top-border"
          style="border-bottom: 1px solid #ddd"
        >
          <td
            :style="channel.target_type == 2 && { 'padding-left': '15px' }"
            class="text-channel-code"
          >
            {{ channel.channel_code }}
          </td>
          <td class="text-target">{{ channel.target.t1 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t1 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t1 || 0 }}</td>
          <td class="text-target">{{ channel.target.t2 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t2 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t2 || 0 }}</td>
          <td class="text-target">{{ channel.target.t3 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t3 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t3 || 0 }}</td>
          <td class="text-target">{{ channel.target.t4 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t4 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t4 || 0 }}</td>
          <td class="text-target">{{ channel.target.t5 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t5 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t5 || 0 }}</td>
          <td class="text-target">{{ channel.target.t6 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t6 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t6 || 0 }}</td>
          <td class="text-target">{{ channel.target.t7 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t7 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t7 || 0 }}</td>
          <td class="text-target">{{ channel.target.t8 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t8 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t8 || 0 }}</td>
          <td class="text-target">{{ channel.target.t9 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t9 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t9 || 0 }}</td>
          <td class="text-target">{{ channel.target.t10 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t10 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t10 || 0 }}</td>
          <td class="text-target">{{ channel.target.t11 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t11 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t11 || 0 }}</td>
          <td class="text-target">{{ channel.target.t12 || 0 }}</td>
          <td v-if="channel.target_type == 1" class="text-danger">
            {{ channel.plan.t12 || 0 }}
          </td>
          <td v-if="channel.target_type == 2" class="text-danger">-</td>
          <td class="text-result">{{ channel.result.t12 || 0 }}</td>
          <td class="text-target" style="font-weight: 600">
            {{ channel.target.total || 0 }}
          </td>
          <td
            v-if="channel.target_type == 1"
            class="text-danger"
            style="font-weight: 600"
          >
            {{ channel.plan.total || 0 }}
          </td>
          <td
            v-if="channel.target_type == 2"
            class="text-danger"
            style="font-weight: 600"
          >
            -
          </td>
          <td class="text-result" style="font-weight: 600">
            {{ channel.result.total || 0 }}
          </td>
          <td
            v-if="item.staff_code != 'Total' && channel.target_type != 2"
            class="text-center"
          >
            <span
              style="
                color: darkcyan;
                text-decoration-line: underline;
                cursor: pointer;
                font-size: 13px;
              "
              @click="
                actionListMapPlan(
                  channel.channel_id,
                  item.staff_code,
                  channel.target_id
                )
              "
              id="detail"
              class="detail"
              data-bs-toggle="modal"
              data-original-title="test"
              data-bs-target="#actionListMapPlan"
            >
              {{ $t("DETAIL") }}</span
            >
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <popupMonth v-model="showPopupMonth" :searchData="props.searchData" :listMapPlan="listMapPlan"/>

</template>
