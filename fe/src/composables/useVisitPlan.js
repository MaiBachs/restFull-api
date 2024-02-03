import axios from "axios";
import { ref } from "vue";
import { notify } from "@kyvg/vue3-notification";
import { useLoaddingStore } from "@/store/LoaddingStore";
export default function useVisitPlan() {
    const loaddingStore = useLoaddingStore();
    const checkListCateResult = ref(false)
    const listCateResult = ref([])
    const checklistrequired = ref(true)
   async function exportSearchResult(searchData) {
    const loaddingStore = useLoaddingStore();
        try {
            loaddingStore.loading = true;
            await axios.post("/api/visit-plan/download-result", searchData, { responseType: 'blob' }).then((response) => { 
            const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            response.headers['content-disposition',`attachment; filename="Template_visit_plan_search_result.xls"`];
            link.setAttribute('download', "Template_visit_plan_search_result.xls");
            document.body.appendChild(link);
            link.click();
            link.remove();
        });
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

    async function getCheckList(planId) {
        checkListCateResult.value = false;
        listCateResult.value = [];
        try {
            loaddingStore.loading = true;
            const response = await axios.post('/api/visit-plan-result/list', {
                visit_plan_id: planId
            });
            if(response.data && response.data.data &&  response.data.data.length) {
                listCateResult.value = response.data.data
console.log(listCateResult);
                checklistrequired.value = true
              if(listCateResult.value  && listCateResult.value.length) {
                checkListCateResult.value = true;
                for (var i in listCateResult.value){
                    if (listCateResult.value[i] && listCateResult.value[i].sub_items &&  listCateResult.value[i].sub_items.length > 0 ) {
                        const checkListLevel2 = listCateResult.value[i].sub_items;
                        for (var a in checkListLevel2) {
                            if(checkListLevel2[a]) {
                                const checktestLevel3 = checkListLevel2[a].sub_items
                                for (var o in checktestLevel3) {
                                    if(checktestLevel3[o].required_file == 1) {
                                        if(checktestLevel3[o].list_file_path && checktestLevel3[o].list_file_path.length) {
                                            for (var list in checktestLevel3[o].list_file_path ) {
                                                console.log(checktestLevel3[o].result)
                                                if(list != null && checktestLevel3[o].result != null && checktestLevel3[o].result != 'false') {
                                                    checklistrequired.value = true
                                                } else {
                                                   return checklistrequired.value = false
                                                }
                                            }
                                        } else {
                                            checklistrequired.value = false
                                        }
                                    } else {
                                        checklistrequired.value = true
                                    }
                                }
                            }
                        }
                    }
                 }
                } else {
                    checklistrequired.value = false;
                }


            checkListCateResult.value = false
            } else {
            checkListCateResult.value = true
            }
            loaddingStore.loading = false;
        } catch (error) {
            loaddingStore.loading = false;
            notify({
            type: "error",
            title: "Visit plan result",
            text: error,
            duration: 500,
            });
        }
    }

    return {
        exportSearchResult,
        getCheckList,
        listCateResult,
        checkListCateResult
    }
}