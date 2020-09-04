import { useRouter } from 'next/router'
import { useState, useEffect } from 'react'
import { Calendar, momentLocalizer } from 'react-big-calendar'
import { useSelector } from 'react-redux'
import moment from 'moment'
import styles from '../../styles/pages/calendar.module.scss'
import axios from 'axios'

function Event({ event }) {
    

    console.log(event)

    return (
        <div className={styles.event}>
            <div className={styles.title}>{event.title}</div>
            <div className={styles.extra}>{event.desc}</div>
        </div>
    );
  }

const Schedule = (req, res) => {
    const user = useSelector(state => (state.user))
    const company = useSelector(state => (state.companyView.company))
    const router = useRouter()
    const localizer = momentLocalizer(moment)
    const [programari,setProgramari]=useState([])
    const scheduleRequest = async (token,company)=>{
        return new Promise((resolve,rej)=>{
            const url =process.env.REQ_HOST+'/schedules/user_specialist/display'
            axios.get(url,{params:{
                'companyUsername':company
            },headers:{
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
            const username = company.username;
            const token = user.token
            const schedules = [];
            scheduleRequest(token,username).then(data=>{
                console.log(data)
                let schedules = []
               for( let i in data){
                   schedules.push({
                       title:data[i].specialistDTO.servicesDTO.name,
                       allDay:false,
                       start: new Date(data[i].s_start),
                       end: new Date(data[i].s_end),
                       desc: 'desc'
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
                components={{
                    event: Event
                  }}
            />
        </div>
    )
}

export default Schedule