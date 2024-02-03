import axios from "axios";
import { notify } from "@kyvg/vue3-notification";
import { useLoaddingStore } from "@/store/LoaddingStore";
export default function useBTS() {

   async function exportSearchResult(searchData) {
    const loaddingStore = useLoaddingStore();
        try {
            loaddingStore.loading = true;
            await axios.post("/api/sale-bts-config/download-result", searchData, { responseType: 'blob' }).then((response) => { 
            const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            response.headers['content-disposition',`attachment; filename="Template_search_bts_export.xls"`];
            link.setAttribute('download', "Template_search_bts_export.xls");
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

    return {
        exportSearchResult
    }
}