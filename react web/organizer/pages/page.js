import { useRouter } from 'next/router'

const page = () => {
    const router = useRouter()
    
    return (
        <span onClick={() => { router.back() }}>lol?</span>
    )
}

export default page;