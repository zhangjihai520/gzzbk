import axios from 'axios'
import { getHeaders } from '@/utils/auth'

export function downloadExcelRequest(url, params) {
    axios({
        url: process.env.VUE_APP_BASE_API + url,
        method: 'post',
        data: params,
        responseType: `arraybuffer`,
        headers: { 'Content-Type': 'application/json;charset=utf-8', ...getHeaders() }
    }).then(res => {
        if (res.status == 200) {
            let blob = new Blob([res.data], {
                type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'
                //word文档为application/msword,pdf文档为application/pdf,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8
            });
            let objectUrl = URL.createObjectURL(blob);
            let link = document.createElement("a");
            let fname = res.headers["content-disposition"]; //下载文件的名字
            link.href = objectUrl;
            link.setAttribute("download", fname);
            document.body.appendChild(link);
            link.click();
        } else {
            this.$message.error("导出失败");
        }
    })
}