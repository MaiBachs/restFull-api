<script setup>
import {defineProps} from "vue";

const props = defineProps({
    listchannelTotalTagetType: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  totalTargetTypeAccumulated :{
    type: Object,
    required: true,
    default: () => ({}),
  },
  showChoseFileBC: {
    type: Boolean,
    required: true,
    default: false
  }
  
});

console.log(props.listchannelTotalTagetType)
</script>
<template>
    <div v-if="props.listchannelTotalTagetType.length && props.showChoseFileBC" class="table-responsive target-plan-bc">
        <table class="table table-bordered table-hover">
            <thead 
            >
            <tr  class="black-muted-bg bg-target">
                <th rowspan="2" class="text-center">{{ $t('HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.JANUARY')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.FEBRUARY')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.MARCH')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.APRIL')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.MAY')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.JUNE')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.JULY')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.AUGUST')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.SEPTEMBER')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.OCTOBER')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.NOVEMBER')}}</th>
                <th colspan="2">{{ $t('DATE.MONTHNAMES.DECEMBER')}}</th>
                <th colspan="2">{{ $t( 'HOME.SEARCH.MAP.ACTION.SALE_INFO.TOTAL')}}</th>
            </tr>
            <tr class="no-top-border target" style="border-bottom: 1px solid #ddd;">
                <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td class="text-target">N</td>
                  <td class="text-color-target-bar">A</td>
                  <td style="font-weight: 600;" class="text-target">N</td>
                  <td style="font-weight: 600;" class="text-color-target-bar">A</td>
            </tr>
            </thead>
            <tbody>
                <tr class="no-top-border target-total-type" v-for="item in props.listchannelTotalTagetType" :key="item" style="border-bottom: 1px solid #ddd;">
                  <td class="text-left" style="text-align: left;">{{item.label}}</td>
                  <td>{{item['targetT1-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT1-2'] || 0}}</td>
                  <td>{{item['targetT2-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT2-2'] || 0}}</td>
                  <td>{{item['targetT3-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT3-2'] || 0}}</td>
                  <td>{{item['targetT4-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT4-2'] || 0}}</td>
                  <td>{{item['targetT5-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT5-2'] || 0}}</td>
                  <td>{{item['targetT6-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT6-2'] || 0}}</td>
                  <td>{{item['targetT7-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT7-2'] || 0}}</td>
                  <td>{{item['targetT8-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT8-2'] || 0}}</td>
                  <td>{{item['targetT9-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT9-2'] || 0}}</td>
                  <td>{{item['targetT10-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT10-2'] || 0}}</td>
                  <td>{{item['targetT11-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT11-2'] || 0}}</td>
                  <td>{{item['targetT12-1'] || 0}}</td>
                  <td class=" text-color-target-bar">{{item['targetT12-2'] || 0}}</td>
                  <td style="font-weight: 600;">{{item['targetTotal-1'] || 0}}</td>
                  <td class=" text-color-target-bar" style="font-weight: 600;">{{item['targetTotal-2'] || 0 }}</td>
               </tr>
            </tbody>
        </table>
        
    </div>

    <div v-if="props.totalTargetTypeAccumulated && props.totalTargetTypeAccumulated.length && !props.showChoseFileBC" class="row col-xs-12 target-plan-bc">
      <div class="target-plan">
         <table id="table_target_br_accumulated" class="table table-hover sale-plan">
            <thead>
               <tr  class="black-muted-bg bg-target" >
                    <th rowspan="2" class="text-center">{{ $t('HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.JANUARY')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.FEBRUARY')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.MARCH')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.APRIL')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.MAY')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.JUNE')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.JULY')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.AUGUST')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.SEPTEMBER')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.OCTOBER')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.NOVEMBER')}}</th>
                    <th>{{ $t('DATE.MONTHNAMES.DECEMBER')}}</th>
                    <th>{{ $t( 'HOME.SEARCH.MAP.ACTION.SALE_INFO.TOTAL')}}</th>
               </tr>
               <tr>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
                 <td class="text-center text-target">{{ $t('ACCUMULATED')}}</td>
               
               </tr>
               
            </thead>
            <tbody>
              <tr class="no-top-border target-total-type" v-for="item in props.listchannelTotalTagetType" :key="item" style="border-bottom: 1px solid #ddd;">
                 <td class="text-left" style="text-align: left;">{{item.label}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT1-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT2-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT3-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT4-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT5-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT6-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT7-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT8-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT9-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT10-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT11-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar">{{item['targetT12-2'] || 0}}</td>
                 <td class="text-center text-color-target-bar" style="font-weight: 600;">{{item['targetTotal-2'] || 0 }}</td>
              </tr>
            </tbody>
         </table>
      </div>
   </div>
</template>

<style scoped>
 .target-plan-bc td ,
    .target-plan-bc th  {
        background-color:  #ffdddd;
        border-right: 1px solid #ddd;
    }
</style>