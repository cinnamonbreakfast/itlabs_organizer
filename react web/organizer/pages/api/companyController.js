import axios from 'axios'
import { company_view_actions as cva } from './redux/companyViewer'

class CompanyController {
    constructor(dispatcher) {
        this.dispatcher = dispatcher
    }

    async getCompany(identifier) {
        return axios.get(process.env.REQ_HOST + '/c/' + identifier)
        .then(resp => {
            if(resp.data) {
                return resp.data
            }
            return null;
        })
        .catch(err => {
            console.log(err)
            if(err.response && err.response.data) return {message: err.response.data, code: 404 }
            return {message: "unknown error", code: 404 }
        })
    }

    dispatchCompany(id) {
        this.getCompany(id).then(r => {
            if(r && this.dispatcher && !r.code) {
                this.dispatcher({type: cva.SET_VIEW_COMPANY, payload: r})
                console.log('set up comp')
            } else {
                console.log('erer1')
                this.dispatcher({type: cva.CLEAR_VIEW_COMPANY})
                this.dispatcher({type: cva.SET_FIND_CODE, payload: 404})
            }
        })
        .catch(e => {
            console.log(e)
            if(this.dispatcher) {
                this.dispatcher({type: cva.SET_FIND_CODE, payload: 404})
                this.dispatcher({type: cva.CLEAR_VIEW_COMPANY})
            }
        })
    }

    async updateService(service, token) {
        console.log(service, token)
        return axios({
            url: process.env.REQ_HOST + '/service/update',
            method: 'PUT',
            data: service,
            headers: {
                'token': token,
                'Content-Type':'multipart/form-data'
            },
        }).then(_resp => {
            console.log(_resp)
            this.dispatchCompany(service.companyUsername)
            return {status: 'ok', message: 'Service was added!'}
        }).catch(_err => {
            console.log(_err)
            console.log(_err.response)
            return {status: 'bad', message: (_err.response || _err.response) || "Unknown error occured. Try again."}
        })
    }

    async deleteService(service, token) {
        return axios({
            url: process.env.REQ_HOST + '/service/delete',
            method: 'DELETE',
            data: service,
            headers: {
                'TOKEN': token,
            },
        }).then(_resp => {
            console.log(_resp)
            console.log(service.companyUsername)
            service.companyUsername && this.dispatchCompany(service.companyUsername)
            return {status: 'ok', message: 'Service was deleted!'}
        }).catch(_err => {
            console.log(_err)
            console.log(_err.response)
            console.log(service.companyUsername)
            console.log(service)
            return {status: 'bad', message: (_err.response || _err.response) || "Unknown error occured. Try again."}
        })
    }

    async sendInvite(invite) {
        let data = new FormData()
        data.set('userPhone', invite.phone)
        data.set('companyUsername', invite.username)
        data.set('serviceName', invite.service)

        return axios({
            method: 'POST',
            url: process.env.REQ_HOST + '/c/invite/specialist',
            data: data,
            headers: {
                'token': invite.token,
            }
        })
        .then(res => {
            console.log(res)
            return {message: res.data, type: 'ok'}
        })
        .catch(err => {
            console.log(err)
            console.log(err.response)
            return {message: (err.response && err.response.data) || "Unknown error. Try again.", type: 'error'}
        })
    }

    async createService(service, token) {
        console.log(service, token)
        return axios({
            url: process.env.REQ_HOST + '/service/create',
            method: 'POST',
            data: service,
            headers: {
                'TOKEN': token,
            },
        }).then(_resp => {
            console.log(_resp)
            this.dispatchCompany(service.companyUsername)
            return {status: 'ok', message: 'Service was added!'}
        }).catch(_err => {
            console.log(_err)
            console.log(_err.response)
            return {status: 'bad', message: (_err.response || _err.response) || "Unknown error occured. Try again."}
        })
    }

    async retrieveTable(id) {
        return axios({
            url: process.env.REQ_HOST + '/tt/service/display/' + id,
            method: 'GET'
        })
        .then(r => {
            return r.data
        })
        .catch(e => {
            console.log(e)
            return []
        })
    }
    async deleteTable( id,token){
        const url = process.env.REQ_HOST + '/tt/delete';
        return axios.delete(url,{
            params:{
                tt:id
            },
            headers:{
                token:token
            }
        }).then(e=>{
            return e.data
        })
        .catch(e =>
            {
                return false
            })
    }
    async createTable(entry, token) {
        console.log(entry)
        return axios({
            url: process.env.REQ_HOST + '/tt/create',
            method: 'POST',
            data: null,
            headers: {
                'TOKEN': token
            },
            params:{
                'companyUsername':entry.companyUsername,
                'day':entry.day,
                'start':entry.start ,
                'end':entry.end,
                'serviceName':entry.serviceName
                       }
        })
        .then(resp => {
            console.log(resp)
            return {message: resp.data, type: 'ok'}
        })
        .catch(err => {
            console.log(err.response)
            if(err.response) return {message: err.response.data, type: 'error'}
            return {message: 'Unknown error', type: 'error'}
        })
    }
    async updateDetails(details, token) {
        let formData = new Data()
        let c = details.company;
        formData.append('file',details.file);
        formData.append('name',c.name);
        formData.append('city',c.city);
        formData.append('address',c.address);
        formData.append('category',c.category);
        formData.append('country',c.country);
        formData.append('username',c.username);
        const url = process.env.REQ_HOST + '/c/changeDetails'
        axios.put(url,formData,{headers:{
            'Content-Type':'multipart/form-data',
            'token':token
        }})
        .then(resp => {
            if(resp.data) {
                return {message: resp.data, type: 'ok'}
            }
            return {message: 'Unknown error?', type: 'error'}
        })
        .catch(err => {
            console.log(err)
            if(err.response.data) {
                return {message: err.response.data, type: 'error'}
            }

            return {message: 'Unknown error', type: 'error'}
        })
    }
}

export default CompanyController