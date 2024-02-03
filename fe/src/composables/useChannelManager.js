import axios from "axios";
import { ref } from "vue";
import { i18n } from "@/main.js";
// import { i18n } from "@/main.js";
import { notify } from "@kyvg/vue3-notification";
import { useLoaddingStore } from "@/store/LoaddingStore";
import { useItemConfigStore } from "@/store/ItemConfigStore";
import { useUserStore } from '@/store/UserStore';

export default function useChannelManager() {
    const itemConfigStore = useItemConfigStore();
    const loaddingStore = useLoaddingStore();
    const userStore = useUserStore()
    const importResults = ref([]);
    const listSearchChannel = ref([]);
    const checkDataSearchChannel = ref(true)
    const listChannelLevel1 = ref([])
    const listChannelLevel2 = ref([])
    const listChannelLevel3 = ref([])
    const listMapPlanChannelDevelops = ref([])
    const listTargetSubmit = ref([])
    const totalChannels = ref([])
    const errorSubmitTarget = ref("")

    async function exportSearchResult(searchData) {
      
        const params = {
            target_level: searchData.target_level,
            br_code: searchData.br_code,
            bc_code: searchData.bc_code,
            channel_id:searchData.channel_id,
            plan_date: searchData.plan_date || ''
        };
        try {
            loaddingStore.loading = true;
            await axios.post("/api/plan-channel/download-file", params, { responseType: 'blob' }).then((response) => { 
            const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            response.headers['content-disposition',`attachment; filename="template_plan_channel_search_result.xls"`];
            link.setAttribute('download', "template_plan_channel_search_result.xls");
            document.body.appendChild(link);
            link.click();
            link.remove();
        });
            loaddingStore.loading = false;
            } catch (error) {
            loaddingStore.loading = false;
                notify({
                type: "error",
                title: "Export download file",
                text: error,
                duration: 500,
                });
            }
    }

    async function exportDetailChannel(searchData) {
        const params = {
            br_code: searchData.br_code,
            bc_code: searchData.bc_code,
            channel_id:searchData.channel_id,
            plan_date: searchData.plan_date || ''
        };
        try {
            loaddingStore.loading = true;
            await axios.post("/api/plan-channel/export-channel-develop-report", params, { responseType: 'blob' }).then((response) => { 
            const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            response.headers['content-disposition',`attachment; filename="template_plan_channel_export_detail.xls"`];
            link.setAttribute('download', "template_plan_channel_export_detail.xls");
            document.body.appendChild(link);
            link.click();
            link.remove();
        });
            loaddingStore.loading = false;
            } catch (error) {
            loaddingStore.loading = false;
                notify({
                type: "error",
                title: "Export channel develop report",
                text: error,
                duration: 500,
                });
            }
    }

    async function importChannelManager(searchData,fileData,typeImport) {
      console.log(searchData)
      if(typeImport && typeImport.value) {
        typeImport = typeImport.value
      }else {
        typeImport = ""
      }
        try {
          const formData = new FormData();
          formData.append("brCode", searchData.br_code);
          formData.append("bcCode", searchData.bc_code);
          formData.append("file", fileData.value);
          formData.append("planDate", searchData.plan_date);
          formData.append("targetLevel", searchData.target_level);
          formData.append("targetType", typeImport);
          loaddingStore.loading = true;
          const response = await axios.post("/api/plan-channel/import", formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          });
          if(response.data) {
            importResults.value = response
          }
          loaddingStore.loading = false;
        } catch (error) {
          loaddingStore.loading = false;
          notify({
            type: "error",
            title: "Import channel manager",
            text: error,
            duration: 500,
          });
        }
    }
    async function searchChannel(searchData) {
      listSearchChannel.value = [];
    
        const params = {
            action: "LIST",
            target_level: searchData.target_level,
            // br_code: searchData.br_code,
            // bc_code: searchData.bc_code,
            channel_id: searchData.channel_id,
            plan_date: searchData.plan_date || ''
        };
        try {
          loaddingStore.loading = true;
          const response = await axios.post("/api/plan-channel/search", params);
          if (response.data.data && response.data.data.target_list.length) {
            checkDataSearchChannel.value = true;
            
          } else {
            checkDataSearchChannel.value = false;
          }
          if(response.data && response.data.data) {
            if(searchData.target_level == 1) {
              listChannelLevel1.value = []
              listChannelLevel1.value = response.data.data.target_list;
            }else if(searchData.target_level == 2) {
              listChannelLevel2.value = []
              listChannelLevel2.value = response.data.data.target_list;
            }else if(searchData.target_level == 3) {
              listChannelLevel3.value = []
              listChannelLevel3.value = response.data.data.target_list;
            }
          }
          
          
          loaddingStore.loading = false;
        } catch (error) {
          loaddingStore.loading = false;
          notify({
            type: "error",
            title: "Get current user",
            text: error,
            duration: 500,
          });
        }
    }
    async function searchTargetSubmit(searchData) {
      listTargetSubmit.value = []
        const params = {
            action: "LIST",
            target_level: searchData.target_level,
            br_code: searchData.br_code,
            plan_date: searchData.plan_date
        };
        try {
          loaddingStore.loading = true;
          const response = await axios.post("/api/plan-channel/search", params);
          
          if(response.data && response.data.data) {
            listTargetSubmit.value = response.data.data.target_list;
              totalChannels.value = [];
                if (listTargetSubmit.value && listTargetSubmit.value.length > 0) {
                    let totalTarget = listTargetSubmit.value.filter(t => t.bc_code == "Total");
                    if(totalChannels.value) {
                        let totalTargetType = [];
                        totalTargetType = totalTarget[0].channels;
                        totalChannels.value = totalTargetType.filter(t => t.target_type == 1);
                    }
                    
                } else {
                    totalChannels.value = [];
                }
          }
          
          
          loaddingStore.loading = false;
        } catch (error) {
          loaddingStore.loading = false;
          notify({
            type: "error",
            title: "Get current user",
            text: error,
            duration: 500,
          });
        }
    }

    function submitChannelBranch() {
      errorSubmitTarget.value = "";
      if (!listChannelLevel2.value) {
          notify({
            type: "error",
            text: i18n.global.t("CLICK_SEARCH"),
            duration: 2000,
          });
          return;
      }
      if (userStore.listchannelTotal.value && totalChannels.value && totalChannels.value.length) {
          for (let i in userStore.listchannelTotal.value) {
              let t = userStore.listchannelTotal.value[i];
              if (t.total_target > 0 && t.total_target != 2) {
                  let totalChannelTypes = totalChannels.value.filter(total => total.channel_id == t.channel_id);
                  if (totalChannelTypes && totalChannelTypes.length > 0) {
                      let totalChannel = totalChannelTypes[0];
                      if (totalChannel.target.total < t.totalTarget) {
                          var errorMsg = totalChannelTypes[0].channel_code+ '(' +totalChannelTypes[0].channel_key +')';
                          errorSubmitTarget.value += errorMsg +';' ;
                      }
                  } else {
                      const errorMsg = t.channel_code+ '(' +t.channel_key +')';
                      console.log(errorMsg)
                      errorSubmitTarget.value += errorMsg +';' ;
                  }
              }
          }
      }  else {
          notify({
            type: "error",
            text: i18n.global.t("NO_TARGET_DEVELOP_NEW"),
            duration: 2000,
          });
          return;
      }
      
      if (!errorSubmitTarget.value) {
        console.log(errorSubmitTarget.value)
          // var brCode = "";
          // if (vm.sucursalChannel && vm.sucursalChannel.code) {
          //     var brCode = vm.sucursalChannel.code
          // }
          // var status = vm.br_submit;
          // var planDate = "";
          // if(vm.yearChannel && vm.yearChannel) {
          //     planDate = moment(vm.yearChannel).format("YYYY");
          // }
          // const searchRequest = JSON.stringify({
          //     'brCode': brCode, 'planDate': planDate, 'status': status
          // });
          // const param = {
          //     action: 'SET_STATUS',
          //     searchRequest

          // };
          // $DataSubmitChannelBr.save(param, function (data) {
          //     if (data.errorCode == null) {
          //         toastr.success($translate.instant("SUBMIT_SUCCESS"));
          //         vm.modalInstance.close();
          //         searchChannelManager()

          //     } else {
          //         vm.errorCode = data.errorMessage;
          //         toastr.warning($translate.instant(vm.errorCode));
          //     }

          // }, function (error) {
          // });
      } else {
          var result = errorSubmitTarget.value.substring(0, errorSubmitTarget.value.length - 1);
          const msgSubmitErorr = result + ' ' + i18n.global.t("TARGET_IS_NOT_ENOUGH");

          notify({
            type: "error",
            text: msgSubmitErorr,
            duration: 2000,
          });
      }


  }
    
    // async function getTotalTargetChannel(searchData,modelBranchCode,modelBCCode) {
    //   console.log(userStore)
    //   listSearchChannel.value = [];
    //   showChoseFileBC.value = []
    //   listParentIds.value = []
    //     const params = {
    //         action: "VIEW",
    //         br_code: modelBranchCode,
    //         bc_code: modelBCCode,
    //         target_level: searchData.target_level - 1,
    //         plan_date: searchData.plan_date || ''
    //     };
    //     try {
    //       loaddingStore.loading = true;
    //       const response = await axios.post("/api/plan-channel/search", params);
    //       console.log(response)
    //       console.log(response.data.data.current_target)
    //       if(response.data && response.data.data && response.data.data.current_target) {
    //         const listChannelObj = {}
    //         response.data.data.current_target.forEach(o => {
    //             const key = `${o.channel_code.replace(' ', '_')}-${o.channel_key}`;
    //             // listChannelObj[']
    //             if (!listChannelObj[key]) {
    //                 listChannelObj[key] = {
    //                     label: `${o.channel_code} - ${o.channel_key}`,
    //                 }
    //             }
    //             listChannelObj[key][`targetT1-${o.target_type}`] = o.t1_target;
    //             listChannelObj[key][`targetT2-${o.target_type}`] = o.t2_target;
    //             listChannelObj[key][`targetT3-${o.target_type}`] = o.t3_target;
    //             listChannelObj[key][`targetT4-${o.target_type}`] = o.t4_target;
    //             listChannelObj[key][`targetT5-${o.target_type}`] = o.t5_target;
    //             listChannelObj[key][`targetT6-${o.target_type}`] = o.t6_target;
    //             listChannelObj[key][`targetT7-${o.target_type}`] = o.t7_target;
    //             listChannelObj[key][`targetT8-${o.target_type}`] = o.t8_target;
    //             listChannelObj[key][`targetT9-${o.target_type}`] = o.t9_target;
    //             listChannelObj[key][`targetT10-${o.target_type}`] = o.t10_target;
    //             listChannelObj[key][`targetT11-${o.target_type}`] = o.t11_target;
    //             listChannelObj[key][`targetT12-${o.target_type}`] = o.t12_target;
    //             listChannelObj[key][`targetTotal-${o.target_type}`] = o.total_target;

    //         });
            
    //         listchannelTotalTagetType.value = Object.values(listChannelObj);
    //         console.log(listchannelTotalTagetType.value)
    //         listchannelTotal.value = response.data.data.current_target;
    //         totalTargetType.value = [];
    //         totalTargetTypeAccumulated.value = [];
    //         console.log(totalTargetType.value)
    //         console.log(listchannelTotal.value)
    //         if (listchannelTotal.value && listchannelTotal.value.length) {
              
    //             if (listchannelTotal.value[0].child_status == statusDraft.value) {
    //               console.log(totalTargetType.value)
    //               showSubmit.value = true
    //             }

    //             for (let i in listchannelTotal.value) {
    //                 if (listchannelTotal.value[i].id) {
    //                     listParentIds.value.push(listchannelTotal.value[i].id);
    //                 }
    //             }
    //             totalTargetType.value = listchannelTotal.value.filter(t => t.target_type == 1);
    //             totalTargetTypeAccumulated.value = listchannelTotal.value.filter(t => t.target_type == 2 && t.total_target != 0);
                
    //             if (totalTargetType.value[0].status == saleApproved.value) {
    //               showChoseFileBC.value = true;
    //             }

    //         }
    //       }
          
          
    //       loaddingStore.loading = false;
    //     } catch (error) {
    //       loaddingStore.loading = false;
    //       notify({
    //         type: "error",
    //         title: "Get current user",
    //         text: error,
    //         duration: 500,
    //       });
    //     }
    // }

    function statusApprovedDecode(status) {
      var statusApprove = null;
      if (!status) {
        statusApprove = i18n.global.t("NO_TARGETS");
      } else if (status == 1 || status == 2) {
        statusApprove = i18n.global.t("SALE_DELIVER_BR");
      } else if (status == 3) {
        statusApprove = i18n.global.t("PLAN_NEED_APPROVAL");
      } else if (status == 5) {
        statusApprove = i18n.global.t("PLAN_REJECT");
      } else if (status == 4) {
        statusApprove = i18n.global.t("CREATE_NEW_PLAN");
      } else {
        statusApprove = "";
      }
      return statusApprove;
    }

    async function getListMapPlan(listMapPlan,detailFromDate,plan_date) {
      listMapPlanChannelDevelops.value = []
        console.log(detailFromDate)
        const params = {
          action: "LIST_MAP_PLAN",
          channel_type_id: listMapPlan.channel_type_id,
          staff_code: listMapPlan.staff_code,
          from_date: detailFromDate+'/'+plan_date,
          current_page: listMapPlan.current_page
      };
      try {
        loaddingStore.loading = true;
        const response = await axios.post("/api/plan-channel/search", params);
        if(response.data && response.data.data && response.data.data.channel_plans) {
          listMapPlanChannelDevelops.value = []
          listMapPlanChannelDevelops.value = response.data.data.channel_plans.channel_develops
        }
        
        
        loaddingStore.loading = false;
      } catch (error) {
        loaddingStore.loading = false;
        notify({
          type: "error",
          title: "Get current user",
          text: error,
          duration: 500,
        });
      }
    }

    function downloadFile(item) {
      itemConfigStore.downloadFileError(item);
    }
    function downloadFileError(fileNameError) {
      itemConfigStore.downloadFileError(fileNameError);
    }

    function statusDecodeBc(status) {
      var statusBc = null;
      if (status == 1) {
          statusBc = i18n.global.t("BC_APPROVED");
      } else if (status == -1) {
          statusBc = i18n.global.t("BC_REJECT");
      } else if (status == 3) {
          statusBc = i18n.global.t("PROPOSED");
      } else if (status == -3) {
          statusBc = i18n.global.t("REFUSE_PROPOSAL");
      } else if (status == -2) {
          statusBc = i18n.global.t("SALE_REFUSED");
      } else if (status == 2) {
          statusBc = i18n.global.t("SALE_APPROVED");
      } else if (status == 4) {
          statusBc = i18n.global.t("PLAN_COMPLETE");
      } else if (status == -4) {
          statusBc = i18n.global.t("CANCEL");
      } else {
          statusBc = '';

      }
      return statusBc;
    }

   async function approvedMapPlanChannel(id,listMapPlan,comment,staffCode,br_code){
        const params = {
          action: "SET_STATUS_PLAN",
          br_code:br_code,
          target_id: listMapPlan.target_id,
          status: 1,
          id: id,
          comment: comment,
          staff_code:staffCode
      };
      try {
        loaddingStore.loading = true;
        const response = await axios.post("/api/plan-channel/search", params);
        console.log(response)
        
        
        loaddingStore.loading = false;
      } catch (error) {
        loaddingStore.loading = false;
        notify({
          type: "error",
          title: "Get current user",
          text: error,
          duration: 500,
        });
      }
    }

    return {
        searchChannel,
        exportSearchResult,
        exportDetailChannel,
        importChannelManager,
        checkDataSearchChannel,
        listSearchChannel,
        importResults,
        listChannelLevel3,
        listChannelLevel2,
        listChannelLevel1,
        statusApprovedDecode,
        downloadFileError,
        downloadFile,
        getListMapPlan,
        listMapPlanChannelDevelops,
        statusDecodeBc,
        approvedMapPlanChannel,
        searchTargetSubmit,
        totalChannels,
        submitChannelBranch
    }

    
}