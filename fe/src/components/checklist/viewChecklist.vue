<script setup>
import searchChannel from '../checklist/searchChannel.vue'
import searchChecklist from './searchChecklist.vue'
import PlanAssignment from './PlanAssignment.vue'
import { getChannelType} from "@/store/getChannelType";
import { getChannelTypeChecklist} from "@/store/getChannelTypeChecklist";
import {getParent} from "@/store/getParent";
import {getParent2} from "@/store/getParent2";
const getlistPlan = getChannelType()
getlistPlan.actionGetListFillPlan()
getlistPlan.actionGetListLevle1()
const getListChannelTypeChecklist = getChannelTypeChecklist();
getListChannelTypeChecklist.actionGetChannelTypeChecklist();
const getlistParent = getParent();
getlistParent.actionGetParent();
const getlistParent2 = getParent2();
getlistParent2.actionGetParent2();
</script>
<template>
  <div class="widgets">
    <div class="row">
      <div class="col-sm-12">
        <div class="card">
          <div class="card-header pb-0">
            <h5>{{ $t('CHANNEL_CHECKLIST') }}</h5>
          </div>
          <div class="card-body">
            <div class="tabbed-card">
              <ul class="nav nav-tabs nav-primary" id="pills-clrtab1" role="tablist">
                
                <li class="nav-item nav-primary">
                  <a class="nav-link active" id="pills-clrhome-tab" data-bs-toggle="pill" href="#pills-clrhome" role="tab" aria-controls="pills-clrhome" aria-selected="true"><i class="icofont icofont-list"></i>{{ $t('PLAN') }}</a>
                </li>
                <li class="nav-item nav-primary">
                  <a class="nav-link" id="pills-clrhome-tab" data-bs-toggle="pill" href="#pills-SearchChannel" role="tab" aria-controls="pills-clrhome" aria-selected="false"><i class="icofont icofont-tasks-alt"></i>{{ $t('CHECKLIST') }}</a>
                </li>
                <li class="nav-item nav-primary">
                  <a class="nav-link" id="pills-clrhome-tab" data-bs-toggle="pill" href="#pills-PlanAssignment" role="tab" aria-controls="pills-clrhome" aria-selected="true"><i class="icofont icofont-contacts"></i>{{ $t('PLAN_ASSIGNMENT') }}</a>
                </li>

              </ul>
              <div class="tab-content" id="pills-clrtabContent1">
                
                <div class="p-t-20 tab-pane fade show active" id="pills-clrhome" role="tabpanel" aria-labelledby="pills-clrhome-tab" >
                  <searchChannel />
                </div>
                <div class="p-t-20 tab-pane fade" id="pills-SearchChannel" role="tabpanel" aria-labelledby="pills-clrprofile-tab">
                  <searchChecklist />
                </div>
                <div class="p-t-20 tab-pane fade" id="pills-PlanAssignment" role="tabpanel" aria-labelledby="pills-clrhome-tab" >
                  <PlanAssignment />
                </div>

              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

