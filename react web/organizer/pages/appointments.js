import { useRouter } from 'next/router'
import { useState, useEffect } from 'react'
import { Calendar, momentLocalizer } from 'react-big-calendar'
import { useSelector } from 'react-redux'
import moment from 'moment'
import styles from '../styles/pages/calendar.module.scss'
import axios from 'axios'

const Appointements = (req, res) => {
    const user = useSelector(state => (state.user))

    const router = useRouter()
    const localizer = momentLocalizer(moment)
    const [programari,setProgramari]=useState([])
    const scheduleRequest = async (token)=>{
        return new Promise((resolve,rej)=>{
            const url =process.env.REQ_HOST+'/schedules/user/display'
            axios.get(url,{headers:{
                'token':token
            }}).then(resp=>{
                resolve(resp.data)
            }).catch(e=>{
                console.log(e);
                rej(false);
            })
        });
    }
    const handleResponse =async ()=>{
        return new Promise(resolve=>{
            const token = user.token
            const schedules = [];
            scheduleRequest(token).then(data=>{
                console.log(data)
                let schedules = []
               for( let i in data){
                   schedules.push({
                       title:data[i].specialistDTO.servicesDTO.name,
                       allDay:false,
                       start: new Date(data[i].s_start),
                       end: new Date(data[i].s_end)
                   })
               }
            
                setProgramari(
               
                    schedules
                    
                )
            })
        });
    }
 
    console.log(router.query)

    useEffect(() => {
     handleResponse()

    }, [router])

    return (
        <div className={styles.calendarWrapper}>
            <Calendar
                localizer={localizer}
                events={programari}
                startAccessor="start"
                endAccessor="end"
            />
        </div>
    )
}

export default Appointements