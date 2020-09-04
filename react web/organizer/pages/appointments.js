import { useRouter } from 'next/router'
import { useState, useEffect } from 'react'
import { Calendar, momentLocalizer } from 'react-big-calendar'
import { useSelector } from 'react-redux'
import moment from 'moment'
import styles from '../styles/pages/calendar.module.scss'
import axios from 'axios'
import { Button, Popover, PopoverHeader, PopoverBody } from 'reactstrap';

const deleteRequest = async ()=>{



}
class Event extends React.Component {
    constructor(props) {
      super(props);
      this.toggle = this.toggle.bind(this);
      this.state = {
        popoverOpen: false
      };
    }
  
    toggle() {
      this.setState({
        popoverOpen: !this.state.popoverOpen
      });
      
    }
    handleDelete(){
        
    }
    
    render() {
      return (
        
        <div className={styles.event} >
            <div className={styles.title}>{this.props.event.title}</div>
            <div>
                <Button id={'tooltip'+this.props.event.back_id} type="button">
                    Details 
                </Button>
                <Popover placement="bottom" isOpen={this.state.popoverOpen} target={'tooltip'+this.props.event.back_id} toggle={this.toggle}>
                    <PopoverBody>
                <div >
                <p> Company : {this.props.event.company}</p>
                <p> Specialist : {this.props.event.name}</p>
                <p> Phone : {this.props.event.phone}</p>
                <Button id = {'tool'+this.props.event.back_id}>
                        Delete
                </Button>
                </div>
                    </PopoverBody>
                </Popover>
            </div>
        </div>
      );
    }
  }
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
                       end: new Date(data[i].s_end),
                       company:data[i].specialistDTO.company.name,
                       name:data[i].specialistDTO.user.name,
                       phone:data[i].specialistDTO.user.phone ,
                       back_id:data[i].id
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

export default Appointements