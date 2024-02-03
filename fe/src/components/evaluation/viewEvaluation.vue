<script setup>
import addEvaluation from '../evaluation/addEvalution.vue'
import configureEvaluation from '../evaluation/configureEvaluation.vue'
import { getBranch } from '../../store/getBranch';
import { getChannelType } from '../../store/getChannelType';
import { getJobsToPlan } from '../../store/getJodChannel';

const getListBranch = getBranch();
getListBranch.actionGetBranch();
const getListJodPlan = getJobsToPlan();
getListJodPlan.selectBoxGetJobsToPlan();
const getListChannelType = getChannelType();
getListChannelType.actionGetChannelType();
</script>
<template>
   <div class="widgets">
      <div class="row">
         <div class="col-sm-12">
            <div class="card">
            <div class="card-header pb-0">
               <h5>{{ $t('EVALUATION') }}</h5>
            </div>
            <div class="card-body">
            <div class="tabbed-card">
               <ul class="nav nav-tabs nav-primary" id="pills-clrtab1" role="tablist">
                  <li class="nav-item">
                     <a @click="changePage" class="nav-link active" id="pills-clrprofile-tab" data-bs-toggle="pill" href="#pills-clrprofile" role="tab" aria-controls="pills-clrprofile" aria-selected="true"><i class="icofont icofont-man-in-glasses"></i>{{$t('CONFIGURE_EVALUATION')}}</a>
                  </li>
                  <li class="nav-item nav-primary">
                     <a @click="changePage"  class="nav-link" id="pills-clrhome-tab" data-bs-toggle="pill" href="#pills-clrhome" role="tab" aria-controls="pills-clrhome" aria-selected="false"><i class="icofont icofont-ui-home"></i>{{ $t('ADD_EVALUATION') }}</a>
                  </li>
                  
               </ul>
               <div class="tab-content" id="pills-clrtabContent1">
                  <div class="p-t-20 tab-pane fade show active" id="pills-clrprofile" role="tabpanel" aria-labelledby="pills-clrprofile-tab">
                     <configureEvaluation />
                  </div>
                  <div class="p-t-20 tab-pane fade" id="pills-clrhome" role="tabpanel" aria-labelledby="pills-clrhome-tab" >
                     <addEvaluation />
                  </div>
               </div>
            </div>
            </div>
            </div>
         </div>
      </div>
   </div>
</template>
  
  