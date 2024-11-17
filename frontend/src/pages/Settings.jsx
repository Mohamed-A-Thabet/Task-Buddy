import { useNavigate } from 'react-router-dom'
import '../css/settings.css'


export default function Settings(){
    const navigate = useNavigate()

    function logout(){
        localStorage.removeItem("token")
        navigate("/login")
    }

    return (
    <div className='settings-container'>
        <div className='setting-option' onClick={logout}>
            <p>Log out</p>
        </div>
    </div>
    )
}