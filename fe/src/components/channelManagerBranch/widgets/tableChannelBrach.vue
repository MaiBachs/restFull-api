<script setup>
import {computed,defineEmits, defineProps, ref, reactive} from "vue";
import Modal from '@/components/common/customModal.vue'
import tableChannelBC from '@/components/channelManagerBranch/widgets/tableChannelBC.vue'
import useChannelManager from "@/composables/useChannelManager"

const props = defineProps({
   listChannelLevel2: {
    type: Object,
    required: true,
    default: () => ({}),
  },searchData: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  
});
const searchDataCommon = reactive({
  br_code: "",
  bc_code: "",
  channel_id: "",
  plan_date: "",
  target_level: 3,
});
const showPopupViewLevel3 = ref(false)
const  { searchChannel, listChannelLevel3} = useChannelManager(ref);
const itemDetail = computed({
  get() {
    return props.searchData;
  },
  set(value) {
    emit('update:modelValue', value)
  }
},);
const emit = defineEmits(['onOk', 'update:itemDetail'])

async function viewDetailBC(br,bc) {
    itemDetail.value.br_code = br
    itemDetail.value.bc_code = bc
    itemDetail.value.target_level = 3
    searchDataCommon.br_code = br
    searchDataCommon.bc_code = bc
    searchDataCommon.target_level = 3
    console.log(itemDetail.value.plan_date)
    searchDataCommon.channel_id =  itemDetail.value.channel_id
    searchDataCommon.plan_date =  itemDetail.value.plan_date
  await searchChannel(searchDataCommon)
 
  showPopupViewLevel3.value = true

}


</script>
<template>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead
                class="table-success"
                style="background-color: #24695c; color: #fff"
            >
            <tr  class="black-muted-bg" style="background: #209e91;color: #fff;">
                <th rowspan="2" class="text-center" style="min-width: 150px;">{{ $t('HOME.SEARCH.COMBOBOX.BC')}}</th>
                <th rowspan="2" class="text-center">{{ $t('HOME.SEARCH.MAP.ACTION')}}</th>
                <th rowspan="2" class="text-center" style="min-width: 320px;">{{ $t('HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.JANUARY')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.FEBRUARY')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.MARCH')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.APRIL')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.MAY')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.JUNE')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.JULY')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.AUGUST')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.SEPTEMBER')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.OCTOBER')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.NOVEMBER')}}</th>
                <th colspan="3" class="text-center">{{ $t('DATE.MONTHNAMES.DECEMBER')}}</th>
                <th colspan="3" class="text-center">{{ $t( 'HOME.SEARCH.MAP.ACTION.SALE_INFO.TOTAL')}}</th>
                <!-- <th colspan="1" class="text-center"></th> -->
            </tr>
            <tr class="no-top-border target" style="border-bottom: 1px solid #ddd;">
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
            <tbody v-for="item in props.listChannelLevel2" :key="item">
            <tr class="no-top-border" style="border-bottom: 1px solid #ddd;" :class="{ 'total-row': item.bc_code === 'Total' }">
                <td :rowspan="item.channels.length + 1" class="text-left" style="vertical-align : middle;text-align:center;">{{item.bc_code}}</td>
                <td :rowspan="item.channels.length + 1"  style=" vertical-align: middle; cursor: pointer;">
                    <span v-if="item.bc_code != 'Total'" class="detail tag label work" @click="viewDetailBC(item.br_code,item.bc_code)" style="color: darkcyan; text-decoration-line: underline; cursor: pointer;
                        font-size: 13px;">{{ $t('DETAIL')}}</span>
                </td>
            </tr>
            <tr v-for="channel in item.channels" :key="channel" :class="{'text-color-target': channel.target_type == 2 ,'total-row': item.bc_code === 'Total'  }" class="no-top-border" style="border-bottom: 1px solid #ddd;"  >
                <td :style="channel.target_type == 2 && {'padding-left': '15px'}" class="text-channel-code">{{channel.channel_code}}</td>
                <td class="text-target">{{channel.target.t1 || 0}}</td>
                <td v-if="channel.target_type == 1"  class="text-danger">{{channel.plan.t1 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t1 || 0}}</td>
                <td class="text-target">{{channel.target.t2 || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger">{{channel.plan.t2 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t2 || 0}}</td>
                <td class="text-target">{{channel.target.t3 || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger">{{channel.plan.t3 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t3 || 0}}</td>
                <td class="text-target">{{channel.target.t4 || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger">{{channel.plan.t4 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t4 || 0}}</td>
                <td class="text-target">{{channel.target.t5 || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger">{{channel.plan.t5 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t5 || 0}}</td>
                <td class="text-target">{{channel.target.t6 || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger">{{channel.plan.t6 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t6 || 0}}</td>
                <td class="text-target">{{channel.target.t7 || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger">{{channel.plan.t7 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t7 || 0}}</td>
                <td class="text-target">{{channel.target.t8 || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger">{{channel.plan.t8 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t8 || 0}}</td>
                <td class="text-target">{{channel.target.t9 || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger">{{channel.plan.t9 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t9 || 0}}</td>
                <td class="text-target">{{channel.target.t10 || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger">{{channel.plan.t10 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t10 || 0}}</td>
                <td class="text-target">{{channel.target.t11 || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger">{{channel.plan.t11 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t11 || 0}}</td>
                <td class="text-target">{{channel.target.t12 || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger">{{channel.plan.t12 || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger">-</td>
                <td class="text-result">{{channel.result.t12 || 0}}</td>
                <td class="text-target" style="font-weight: 600;">{{channel.target.total || 0}}</td>
                <td v-if="channel.target_type == 1"  class=" text-danger" style="font-weight: 600;">{{channel.plan.total || 0}}</td>
                <td v-if="channel.target_type == 2" class="text-danger" style="font-weight: 600;">-</td>
                <td class="text-result" style="font-weight: 600;">{{channel.result.total || 0}}</td>
                <!-- <td class="text-center">
                    <button class="detail">{{ 'DETAIL' | translate}}</button>
                    </td> -->
            </tr>
            </tbody>
        </table>
        
    </div>

    <Modal v-model="showPopupViewLevel3" :title="$t('CHANNEL_MANAGEMENT')">
      <tableChannelBC :listChannelLevel3= "listChannelLevel3"/>
  </Modal>
</template>