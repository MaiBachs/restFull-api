<script setup>
import { getUser } from "@/store/getUser";
import searchSurveyConfig from '@/components/surveyConfig/searchSurveyConfig.vue';
import addSurveyConfig from '@/components/surveyConfig/addSurveyConfig.vue';
const listUser = getUser();
listUser.actionGetlistUser();
</script>
<template>
   <div class="widgets">
      <div class="row">
         <div class="col-sm-12">
            <div class="card">
            <div class="card-header pb-0">
               <h5>{{ $t("SURVEY_CONFIG") }}</h5>
            </div>
            <div class="card-body">
            <div class="tabbed-card">
               <ul class="nav nav-tabs nav-primary" id="pills-clrtab1" role="tablist">
                  <li class="nav-item">
                     <a class="nav-link" id="pills-clrprofile-tab" data-bs-toggle="pill" href="#pills-clrprofile" role="tab" aria-controls="pills-clrprofile" aria-selected="false"><i class="icofont icofont-man-in-glasses"></i>{{ $t('CONFIGURE_SURVEY') }}</a>
                  </li>
                  <li class="nav-item nav-primary">
                     <a class="nav-link active" id="pills-clrhome-tab" data-bs-toggle="pill" href="#pills-clrhome" role="tab" aria-controls="pills-clrhome" aria-selected="true"><i class="icofont icofont-ui-home"></i>{{ $t('ADD_SURVEY') }}</a>
                  </li>
               </ul>
               <div class="tab-content" id="pills-clrtabContent1">
                  <div class="p-t-20 tab-pane fade" id="pills-clrprofile" role="tabpanel" aria-labelledby="pills-clrprofile-tab">
                     <searchSurveyConfig />
                  </div>
                  <div class="p-t-20 tab-pane fade show active" id="pills-clrhome" role="tabpanel" aria-labelledby="pills-clrhome-tab" >
                     <addSurveyConfig />
                  </div>
               </div>
            </div>
            </div>
            </div>
         </div>
      </div>
   </div>
</template>
  
  