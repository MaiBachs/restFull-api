<script setup>
import addItemEvaluation from '../itemEvaluation/viewItemadd.vue'
import configureItemEvaluation from '../itemEvaluation/viewItemConfigure.vue';
import { getGroups} from './../../store/getGroup'
import { getJobsToPlan} from './../../store/getJodChannel'
import { getGravedad} from '@/store/getListGravedad'
const listGravedad = getGravedad()
const listGroup = getGroups();
const listJod = getJobsToPlan();
listGroup.actionGetGroup();
listJod.actionGetJobsToPlan();
listGravedad.actionGetListGravedad()

</script>
<template>
   <div class="widgets">
      <div class="row">
         <div class="col-sm-12">
            <div class="card">
            <div class="card-header pb-0">
               <h5>{{$t('ITEMS_EVALUATION')}}</h5>
            </div>
            <div class="card-body">
            <div class="tabbed-card">
               <ul class="nav nav-tabs nav-primary" id="pills-clrtab1" role="tablist">
                  <li class="nav-item">
                     <a class="nav-link active" id="pills-clrprofile-tab" data-bs-toggle="pill" href="#pills-clrprofile" role="tab" aria-controls="pills-clrprofile" aria-selected="false"><i class="icofont icofont-man-in-glasses"></i>{{$t('CONFIGURE_ITEMS_EVALUATION')}}</a>
                  </li>
                  <li class="nav-item nav-primary">
                     <a class="nav-link" id="pills-clrhome-tab" data-bs-toggle="pill" href="#pills-clrhome" role="tab" aria-controls="pills-clrhome" aria-selected="true"><i class="icofont icofont-ui-home"></i>{{$t('ADD_ITEMS_EVALUATION')}}</a>
                  </li>
               </ul>
               <div class="tab-content" id="pills-clrtabContent1">
                  <div class="p-t-20 tab-pane fade" id="pills-clrhome" role="tabpanel" aria-labelledby="pills-clrhome-tab" >
                     <addItemEvaluation />
                  </div>
                  <div class="p-t-20 tab-pane fade show active" id="pills-clrprofile" role="tabpanel" aria-labelledby="pills-clrprofile-tab">
                     <configureItemEvaluation />
                  </div>
               </div>
            </div>
            </div>
            </div>
         </div>
      </div>
   </div>
</template>
  
  