import { useRouter } from 'next/router'
import { useState, useEffect } from 'react'
import { Calendar, momentLocalizer } from 'react-big-calendar'
import { useSelector } from 'react-redux'
import moment from 'moment'
import styles from '../../styles/pages/calendar.module.scss'
import axios from 'axios'
import { Button, Popover, PopoverHeader, PopoverBody } from 'reactstrap';


const Event =(props)=>{
    const user = useSelector(state => (state.user))
    const token = user.token;
    const [popOverOpen,setPopOverOpen]= useState(false)
    const request = props.request
    const toggle = ()=> {
      setPopOverOpen(!popOverOpen)
    }
    const handleDelete= ()=>{
        //axios.post()
        console.log('kill me')
        const url = process.env.REQ_HOST +'/schedule/delete'
        let scheduleId= props.event.back_id
        let data = new FormData()
        data.set('scheduleId',scheduleId);
        axios.delete(url,
        {
            data:data,
            headers:{
                token :token,
                'Content-Type':'multipart/form-data'
            }
        }).then(res=>{
            console.log(res)
            props.event.request()

        }).catch(err=>{
            console.log(err)
        })
    }
      return (
        <div className={styles.event} >
            <div className={styles.title}>{props.title}</div>
            <div>
                <Button id={'tooltip'+props.event.back_id} type="button">
                    Details 
                </Button>
                <Popover placement="bottom" isOpen={popOverOpen} target={'tooltip'+props.event.back_id} toggle={toggle}>
                    <PopoverBody>
                <div >
                    <p> Customer : {props.event.name}</p>
                    <p> Phone : {props.event.phone}</p>
                <Button id = {'tool'+props.event.back_id} onClick={handleDelete}>
                        Delete
                </Button>
                </div>
                    </PopoverBody>
                </Popover>
            </div>
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
            scheduleRequest(token,username).then(data=>{
                console.log(data)
                let schedules = []
               for( let i in data){
                   schedules.push({
                       back_id:data[i].id,
                       title:data[i].specialistDTO.servicesDTO.name,
                       allDay:false,
                       start: new Date(data[i].s_start),
                       end: new Date(data[i].s_end),
                       phone:data[i].userDTO.phone,
                       name:data[i].userDTO.name,
                       request:handleResponse
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