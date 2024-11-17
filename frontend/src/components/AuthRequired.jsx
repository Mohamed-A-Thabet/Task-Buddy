import { Outlet, Navigate } from "react-router-dom"

export default function AuthRequired(){
    if(!localStorage.getItem('token')){
        return <Navigate to="/login" />
    }
    return <Outlet />
}