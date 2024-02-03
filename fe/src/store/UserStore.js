import { defineStore } from 'pinia';
import axios from "axios"
import { useLoaddingStore } from './LoaddingStore';
import { notify } from "@kyvg/vue3-notification";
import { i18n } from "@/main.js";


export const useUserStore = defineStore('UserStore', {

    state: () => {
        return {
            currentUser: { "staff_code": "Loadding", "full_name":"Loadding", "shop_name":"Loadding" },
            checkRoleMenuLatLong: false,
            checkRoleMenuGetLog: false,
            checkRoleMenuPolicy:false,
            checkRoleMenuBTS:false,
            checkRoleMenuCheckList: false,
            checkRoleMenuStrategy: false,
            userType: [],
            checkRoleMenuChannelManager: false,
            checkRoleMenumsmSiteSurvey: false,
            listBranch: [{...0}],
            newItem: { value: 7282, label: "---" + i18n.global.t('STATUS.ALL') + "--- ", code: "---" + i18n.global.t('STATUS.ALL') + "---"},
            roleCheckListResult: false,
            listStaff: [],
            listBC: [],
            modelBranchCode: "",
            modelBCCode: "",
            listchannelTotalTagetType : [],
            listchannelTotal : [],
            totalTargetType : [],
            totalTargetTypeAccumulated : [],
            saleApproved : 4,
            showChoseFileBC : false,
            listParentIds : [],
            showSubmit : false,
            statusDraft : 2,
            totalTargetTypeNewDevelop: [],
            listUserType: []



        }
    },
    
    getters: {
    },
    actions: {
        async fetchCurrentUser() {
            const loaddingStore = useLoaddingStore()
            this.checkRoleMenuLatLong  = false;
            this.checkRoleMenuCheckList = false;
            this.checkRoleMenuGetLog = false;
            this.checkRoleMenuPolicy = false;
            this.checkRoleMenuBTS = false;
            this.checkRoleMenuStrategy = false;
            this.checkRoleMenuChannelManager = false;
            this.checkRoleMenumsmSiteSurvey = false
            try {
                loaddingStore.loading = true;
                const response = await axios.get('/api/users/information')
                this.currentUser = response.data

                const valuesArray = Object.values(this.currentUser.roles);
                this.checkRoleMenuLatLong  = valuesArray.includes('msm.coordernadas.updation');
                this.checkRoleMenuCheckList = valuesArray.includes('msm.checklist.manage');
                this.checkRoleMenuGetLog = valuesArray.includes('msm.check.log');
                this.checkRoleMenuStrategy = valuesArray.includes('map.plan_sale.strategy');
                this.checkRoleMenuBTS = valuesArray.includes('map.plan_sale.strategy');
                this.roleCheckListResult = valuesArray.includes('msm_verify_visit_plan_result');
                this.checkRoleMenumsmSiteSurvey = valuesArray.includes('msm_site_survey');
                this.listUserType = this.currentUser.user_types.map(function (o) {
                    return {
                            value: o.value,
                            label: o.name
                        };
                    })
                    console.log(this.listUserType )
                await this.fetchDataGetBranch()
                
                // if(this.currentUser) {
                //     if (this.currentUser && this.currentUser.shop_type) {
                //         if (this.currentUser.shop_type == "VTP" || this.currentUser.shop_type == "UNKNOWN") {
                //             loaddingStore.loading = true;
                //             const response = await axios.post('/api/shop/shop-tree', {
                //                 level: "3",
                //                 parent_shop_id: "7282"
                //             });

                //             return axios.get('URL_API').then(response => {
                //                 commit('setA', response.data); // Gửi kết quả API vào mutation
                //               }).catch(error => {
                //                 console.error('Error fetching data:', error);
                //               });

                            
                //             // if(response.data && response.data.length) {
                //             //     this.listBranch =  response.data.map(function (o) {
                //             //         return {
                //             //             value: o.shop_id,
                //             //             label: o.code + "-" + o.name,
                //             //             code: o.code
                //             //         };
                //             //     })
                //             //     this.listBranch.unshift(this.newItem);
                //             // }
                //             loaddingStore.loading = false;
                //         }
                //         // if (this.currentUser.shop_type == "BR" && this.currentUser.branch) {
                //         //     this.listBranch =  [this.currentUser.branch].map(function (o) {
                //         //         return {
                //         //             value: o.shop_id,
                //         //             label: o.code + "-" + o.name,
                //         //             code: o.code
                //         //         };
                //         //     })
                //         //     console.log(this.listBranch)
                //         // }
                //         // if (this.currentUser.shop_type == "BC" && this.currentUser.branch && this.currentUser.bc) {
                //         //     this.listBranch =  [this.currentUser.branch].map(function (o) {
                //         //         return {
                //         //             value: o.shop_id,
                //         //             label: o.code + "-" + o.name,
                //         //             code: o.code
                //         //         };
                //         //     })

                //         // }

                //         // if (this.currentUser.shop_type == "AGENT") {
                //         //     this.listBranch =  [this.currentUser.branch].map(function (o) {
                //         //         return {
                //         //             value: o.shop_id,
                //         //             label: o.code + "-" + o.name,
                //         //             code: o.code
                //         //         };
                //         //     })
                //         //     // if (vm.userProfile.branch) {
                //         //     //     vm.listSucursals = [vm.userProfile.branch];
                //         //     //     vm.allBranchs = [vm.userProfile.branch];
                //         //     //     vm.sucursalChannel = vm.listSucursals[0];
                //         //     // }
                //         //     // if (vm.userProfile.bc) {
                //         //     //     vm.centrodenegocios = [vm.userProfile.bc];
                //         //     //     vm.centrodenegocioChannel = vm.centrodenegocios[0];
                //         //     // } else {
                //         //     //     if (vm.centrodenegocioChannel) {
                //         //     //         $scope.getZonalAgent(vm.centrodenegocioChannel);
                //         //     //     } else if (vm.sucursalChannel) {
                //         //     //         $scope.getZonalAgent(vm.sucursalChannel);
                //         //     //     }

                //         //     // }


                //         // }

                //     }
                // }

                
                loaddingStore.loading = false;

            }
            catch (error) {
                loaddingStore.loading = false;
                console.log(error)
                notify({
                    type: 'error',
                    title: "Get current user",
                    text: error,
                    duration: 500,
                  });
                
            }
        },

        

        async fetchDataGetBranch() {
            if (this.currentUser && this.currentUser.shop_type) {
                try {
                    if (this.currentUser.shop_type === "VTP" || this.currentUser.shop_type === "UNKNOWN") {
                        console.log(1)
                        const response = await axios.post('/api/shop/shop-tree', {
                        level: "3",
                        parent_shop_id: "7282"
                        });
                       
                        this.listBranch = response.data.map(function (o) {
                        return {
                            value: o.shop_id,
                            label: o.code + "-" + o.name,
                            code: o.code
                        };
                        
                        });
                        this.listBranch.unshift(this.newItem);
                        
                    } else if (this.currentUser.shop_type === "BR" && this.currentUser.branch) {
                        this.modelBranchCode = this.currentUser.branch.code
                        this.modelBCCode = ""
                        const listBranch = [this.currentUser.branch].map(function (o) {
                            return {
                                value: o.shop_id,
                                label: o.code + "-" + o.name,
                                code: o.code
                            };
                        });
                        this.listBranch = listBranch;
                        this.getBC()
                    } else if (this.currentUser.shop_type === "BC" && this.currentUser.branch && this.currentUser.bc) {
                        this.modelBranchCode = this.currentUser.branch.code
                        this.modelBCCode = this.currentUser.bc.code
                        const listBranch = [this.currentUser.branch].map(function (o) {
                        return {
                            value: o.shop_id,
                            label: o.code + "-" + o.name,
                            code: o.code
                        };
                        });
                        this.listBranch = listBranch;

                        const listBC = [this.currentUser.bc].map(function (o) {
                            return {
                                value: o.shop_id,
                                label: o.code + "-" + o.name,
                                code: o.code
                            };
                        });
                        this.listBC = listBC;
                        this.getStaff()
    
                    } else if (this.currentUser.shop_type === "AGENT") {
                        const listBranch = [this.currentUser.branch].map(function (o) {
                        return {
                            value: o.shop_id,
                            label: o.code + "-" + o.name,
                            code: o.code
                        };
                        });
                        const listBC = [this.currentUser.bc].map(function (o) {
                            return {
                                value: o.shop_id,
                                label: o.code + "-" + o.name,
                                code: o.code
                            };
                        });
                        this.listBC = listBC;
                        this.listBranch = listBranch;
    
                    }
                } catch (error) {
                console.error('Error fetching data:', error);
                throw error;
                }
                return Promise.resolve(null);
    
                }
        },
        async getBC() {
            this.listStaff = []
            this.listBC = []
            const loaddingStore = useLoaddingStore()
            try {
                loaddingStore.loading = true;
                const response = await axios.post('/api/shop/shop-tree', {
                    level: "4",
                    parent_shop_id: this.currentUser.branch.shop_id
                });
                if(response.data && response.data && response.data.length) {
                    this.listBC = response.data.map(function(o) {
                        return {
                            value: o.shop_id,
                            label: o.code + "-" + o.name,
                            code: o.code
                        }
                    })
                    this.listBC.unshift(this.newItem);
                    console.log(this.listBC)
                }
                loaddingStore.loading = false;
            } catch (error) {
                loaddingStore.loading = false;
                notify({
                    type: "error",
                    title: "Get shop tree bc",
                    text: error,
                    duration: 500,
                });
            }
        },
        async  getStaff () {
            const loaddingStore = useLoaddingStore()
            this.listStaff = []
            try {
                loaddingStore.loading = true;
                const response = await axios.post('/api/staff/search', {
                    shop_id: this.currentUser.branch.shop_id,
                    user_type: "USER_AF_AC"
                });

                if(response.data && response.data.data) {
                this.listStaff = response.data.data.map(function(o) {
                    return {
                    value: o.id,
                    label: o.code + '-' + o.name
                    }
                })
                this.listStaff.unshift(this.newItem);
                console.log(this.listStaff)
                }
                loaddingStore.loading = false;
            } catch (error) {
                loaddingStore.loading = false;
                notify({
                    type: "error",
                    title: "Get staff search",
                    text: error,
                    duration: 500,
                });
            }
        },

        async getTotalTargetChannel(searchData) {
            const loaddingStore = useLoaddingStore()
            this.listSearchChannel = [];
            this.showChoseFileBC = false
            this.listParentIds = []
            if(searchData.checkBRBC == true) {
                this.modelBCCode = this.modelBranchCode
            }
              const params = {
                  action: "VIEW",
                  br_code: this.modelBranchCode,
                  bc_code: this.modelBCCode,
                  target_level: searchData.target_level - 1,
                  plan_date: searchData.plan_date || ''
              };
              try {
                loaddingStore.loading = true;
                const response = await axios.post("/api/plan-channel/search", params);
                console.log(response)
                console.log(response.data.data.current_target)
                if(response.data && response.data.data && response.data.data.current_target) {
                  const listChannelObj = {}
                  response.data.data.current_target.forEach(o => {
                      const key = `${o.channel_code.replace(' ', '_')}-${o.channel_key}`;
                      // listChannelObj[']
                      if (!listChannelObj[key]) {
                          listChannelObj[key] = {
                              label: `${o.channel_code} - ${o.channel_key}`,
                          }
                      }
                      listChannelObj[key][`targetT1-${o.target_type}`] = o.t1_target;
                      listChannelObj[key][`targetT2-${o.target_type}`] = o.t2_target;
                      listChannelObj[key][`targetT3-${o.target_type}`] = o.t3_target;
                      listChannelObj[key][`targetT4-${o.target_type}`] = o.t4_target;
                      listChannelObj[key][`targetT5-${o.target_type}`] = o.t5_target;
                      listChannelObj[key][`targetT6-${o.target_type}`] = o.t6_target;
                      listChannelObj[key][`targetT7-${o.target_type}`] = o.t7_target;
                      listChannelObj[key][`targetT8-${o.target_type}`] = o.t8_target;
                      listChannelObj[key][`targetT9-${o.target_type}`] = o.t9_target;
                      listChannelObj[key][`targetT10-${o.target_type}`] = o.t10_target;
                      listChannelObj[key][`targetT11-${o.target_type}`] = o.t11_target;
                      listChannelObj[key][`targetT12-${o.target_type}`] = o.t12_target;
                      listChannelObj[key][`targetTotal-${o.target_type}`] = o.total_target;
      
                  });
                  
                  this.listchannelTotalTagetType = Object.values(listChannelObj);
                  console.log(this.listchannelTotalTagetType)
                  this.listchannelTotal = response.data.data.current_target;
                  this.totalTargetType = [];
                  this.totalTargetTypeAccumulated = [];
                  if (this.listchannelTotal && this.listchannelTotal.length) {
                    
                      if (this.listchannelTotal[0].child_status == this.statusDraft) {
                        console.log(this.totalTargetType)
                        this.showSubmit = true
                      }
      
                      for (let i in this.listchannelTotal) {
                          if (this.listchannelTotal[i].id) {
                            this.listParentIds.push(this.listchannelTotal[i].id);
                          }
                      }
                      this.totalTargetType = this.listchannelTotal.filter(t => t.target_type == 1);
                      this.totalTargetTypeAccumulated = this.listchannelTotal.filter(t => t.target_type == 2 && t.total_target != 0);
                      this.totalTargetTypeNewDevelop = this.listchannelTotal.filter(t => t.target_type == 1 && t.target_type != 0);
                      
                      if (this.totalTargetType[0].status == this.saleApproved) {
                        this.showChoseFileBC = true;
                      }
      
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
        
            
        },

        
        
        
          
});
  
