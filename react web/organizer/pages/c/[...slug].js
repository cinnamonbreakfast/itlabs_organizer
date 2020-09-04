import { useRouter } from 'next/router'
import { useState, useEffect } from 'react'
import { Calendar, momentLocalizer } from 'react-big-calendar'
import { useSelector } from 'react-redux'
import moment from 'moment'
import styles from '../../styles/pages/calendar.module.scss'
import axios from 'axios'
import ReactHover ,{Trigger,Hover }from 'react-hover'

function Event({ event }) {
    
    return (
        <div className={styles.event}>
            <div className={styles.title}>{event.title}</div>
            <div className={styles.extra}>
                <p>Customer {event.name}</p>
                <p>Phone {event.phone}</p>
            
                
            </div>
            <ReactHover options={optionsCursorTrueWithMargin}>
                <Trigger type="trigger">
                    <h1 style={{ background: '#abbcf1', width: '200px' }}> Hover on me </h1>
                </Trigger>
                <Hover type="hover">
                    <h1> I am hover HTML </h1>
                </Hover>
            </ReactHover>
            
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
                       phone:data[i].userDTO.phone,
                       name:data[i].userDTO.name
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