import { createRouter, createWebHashHistory } from 'vue-router'
import BodyView from '../layout/BodyView.vue'
import dashbord from "../components/dashbord/dashbordViewMap.vue"
import carePlan from "@/components/vistiPlan/viewVisit.vue";
import viewEvaluation from "../components/evaluation/viewEvaluation.vue"
import viewItemEvaluation from "../components/itemEvaluation/viewItemEvaluation.vue"
import ViewPenalty from "../components/penalty/viewPenalty.vue"
import ViewBTS from "../components/bts/viewBTS.vue";
import viewPolicy from "../components/bts/viewPolicy.vue"
import viewAuditorEvaluation from "../components/AuditorEvaluation/viewAuditorEvaluation.vue"
import viewLocation from "../components/LocationChannel/viewLocation.vue"
import viewChecklist from "../components/checklist/viewChecklist.vue"
import addCheckList from "../components/checklist/checklistManagement.vue"
import viewChannelManager from "../components/channelManager/viewChannelManager.vue"
import viewManageEvalution from "../components/manageEvaluation/viewManageEvalution.vue"
import viewgetLog from "../components/getLogMSM/viewgetLog.vue"
import viewStrategyPlan from "../components/strategyPlan/viewStrategyPlan.vue"
import viewChannelBranch from "@/components/channelManagerBranch/viewChannelBranch.vue"
import viewChannelBC from "@/components/channelmanagerBC/viewChannelBC.vue"
import viewSurveyConfig from "@/components/surveyConfig/viewSurveyConfig.vue"
const routes = [
  {
    path: '/',
    component: BodyView,
    children: [
      {
        path: "/",
        component: dashbord
      },
      {
        path: "/evaluation",
        component: viewEvaluation
      },
      {
        path: "/item-evaluation-config",
        component: viewItemEvaluation
      },
      {
        path: "/auditorEvaluation",
        component: viewAuditorEvaluation
      },
      {
        path: "/penalty-config",
        component: ViewPenalty
      },
      {
        path: "/location-update-objectType",
        component: viewLocation
       
      },
      {
        path: "/salepolicyManagement",
        component: viewPolicy
      },
      {
        path: "/searchChecklist",
        component: viewChecklist
      },
      {
        path: "/add-new-checkList",
        component: addCheckList
      },
      {
        path: "/btsManagement",
        component: ViewBTS
      },
      {
        path: "/getLog",
        component: viewgetLog
      },
      {
        path: "/care-plan",
        component: carePlan

      },
      {
        path: "/channelManager",
        component: viewChannelManager
      },

      {
        path: "/manageEvalution",
        component: viewManageEvalution
      },
      {
        path: "/strategyPlan",
        component: viewStrategyPlan
      },
      {
        path: "/viewChannelBranch",
        component: viewChannelBranch
      },
      {
        path: "/viewChannelBC",
        component: viewChannelBC
      },
      {
        path: "/survey-config",
        component: viewSurveyConfig
      },
      
      
    ]
  },


]

const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes
})

export default router
