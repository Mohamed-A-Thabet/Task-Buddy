import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import api from '../api/axiosConfig.js'
import '../css/home.css'


export default function Home(){
    const [categories, setCategories] = React.useState([])
    const [newCategory, setNewCategory] = React.useState(null)
    const [editingCategory, setEditingCategory] = React.useState(null)
    const [deletingCategory, setDeletingCategory] = React.useState(null)
    const token = localStorage.getItem('token')
    const navigate = useNavigate()

    React.useEffect(() => {
        async function fetchCategories() {
            const cats = await getCategories()
            setCategories(cats)
        }
        fetchCategories()
    }, [])

    async function getCategories(){
        try {
            const response = await api.get('/categories',
                {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            return response.data
        } 
        catch (error) {
            console.error('Categories get error:', error)
            navigate('/login')
        }
    }

    function clearPastActions(){
        setNewCategory(null)
        setEditingCategory(null)
        setDeletingCategory(null)
    }

    function handleStartAddingCategory(){
        if(!newCategory){
            clearPastActions()
            setNewCategory({
                name: "",
                color: "white",
                adding: true
            })
        }
    }
    
    function handleNewCategoryChange(event){
        const {name, value} = event.target
        setNewCategory(prevCategory => {
            return {
                ...prevCategory,
                [name]: value
            }
        })
    }
    
    async function handleAddCategory(){
        try {
            if(newCategory.name.trim().length === 0)
                return
            const response = await api.post('/categories', 
                {
                    name: newCategory.name,
                    color: newCategory.color,
                }, 
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            const cats = await getCategories(); 
            setCategories(cats); 
        } 
        catch (error) {
            console.error('Categories post error:', error)
            navigate('/login')
        }
        finally {
            clearPastActions()
        }
    }
    
    function handleStartEditingCategory(category){
        clearPastActions()
        const {idInString, name, color} = category
        setEditingCategory({
            name: name,
            color: color,
            id: idInString
        })
    }
    
    function handleEditingCategoryChange(event){
        const {name, value} = event.target
        setEditingCategory(prevCategory => {
            return {
                ...prevCategory,
                [name]: value
            }
        })
    }
    
    async function handleEditCategory(){
        try {
            const response = await api.patch(`/categories/${editingCategory.id}`, 
                {
                    name: editingCategory.name,
                    color: editingCategory.color,
                }, 
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            const cats = await getCategories(); 
            setCategories(cats); 
        } 
        catch (error) {
            console.error('Categories edit error:', error)
            navigate('/login')
        }
        finally {
            clearPastActions()
        }
    }

    async function handleDeleteCategory(){
        try {
            const response = await api.delete(`/categories/${deletingCategory.idInString}`,  
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            const cats = await getCategories(); 
            setCategories(cats); 
        } 
        catch (error) {
            console.error('Categories delete error:', error)
            navigate('/login')
        }
        finally{
            clearPastActions()
        }
    }

    if(!categories || categories.length === 0){
        return <div className="loading">Loading...</div>
    }

    return (
    <div className="categories-container">
        {categories.map(category => {
            return editingCategory?.id == category.idInString? 
            (
                <div key={category.idInString} className="category-tile-container adding-category-tile white">
                    <div className="category-name-container">
                        <label htmlFor="name">Name</label>
                        <input 
                            id="name"
                            name="name"
                            type="text" 
                            value={editingCategory.name}
                            onChange={handleEditingCategoryChange}
                            onKeyDown={(e) => e.key === 'Enter' && handleEditCategory()}
                            autoFocus
                        />
                    </div>
                    <div className="category-color-container">
                        <select value={editingCategory.color} id="color" name="color" onChange={handleEditingCategoryChange}>
                            <option value="white" label="White"></option>
                            <option value="red" label="Red"></option>
                            <option value="blue" label="Blue"></option>
                            <option value="green" label="Green"></option>
                            <option value="orange" label="Orange"></option>
                        </select>
                    </div>
                    <div className="category-buttons-container">
                        <button onClick={clearPastActions}>cancel</button>
                        <button onClick={handleEditCategory}>update</button>
                    </div>
                </div>
            ) : (
                <Link to={`/category/${category.idInString}`} key={category.idInString}>
                    <div key={category.idInString} className={`category-tile-container ${category.color}`}>
                        <h2 className='category-name-text'>{category.name}</h2>
                        {!category.static && (
                            <div className='category-actions-container'>
                                <i  
                                    className="fa-regular fa-pen-to-square"
                                    onClick={(e) => {
                                        e.preventDefault()
                                        handleStartEditingCategory(category)
                                    }} 
                                ></i>
                                <i
                                    className="fa-solid fa-trash"
                                    onClick={(e) => {
                                        e.preventDefault()
                                        clearPastActions()
                                        setDeletingCategory(category)
                                    }} 
                                ></i>
                            </div>
                        )}
                    </div>
                </Link>
            )
        })}

        
        {newCategory?
        (
            <div className="category-tile-container adding-category-tile white">
                <div className="category-name-container">
                    <label htmlFor="category-name">Name</label>
                    <input 
                        id="category-name"
                        name="name"
                        type="text" 
                        placeholder="Homework"
                        value={newCategory.name}
                        onChange={handleNewCategoryChange}
                        onKeyDown={(e) => e.key === 'Enter' && handleAddCategory()}
                        autoFocus
                    />
                </div>
                <div className="category-color-container">
                    <select value={newCategory.color} id="color" name="color" onChange={handleNewCategoryChange}>
                        <option value="white" label="White"></option>
                        <option value="red" label="Red"></option>
                        <option value="blue" label="Blue"></option>
                        <option value="green" label="Green"></option>
                        <option value="orange" label="Orange"></option>
                    </select>
                </div>
                <div className="category-buttons-container">
                    <button onClick={clearPastActions}>cancel</button>
                    <button onClick={handleAddCategory}>create</button>
                </div>
            </div>
        ) : (
            <div onClick={handleStartAddingCategory} className="category-tile-container adding-category-container">
                <h2 className='category-name-text'>Add Category</h2>
            </div>
        )}

        {deletingCategory && (
            <div
                className="deleting-category-overlay"
                onClick={clearPastActions}
            >
                <div
                    className="deleting-category-modal"
                    onClick={(e) => e.stopPropagation()}
                >
                    <p>Are you sure you want to delete <strong>{deletingCategory.name}</strong>? 
                        All associated tasks, regardless of completion, will also be deleted.
                    </p>
                    <div className="deleting-category-buttons-container">
                        <button onClick={clearPastActions}>cancel</button>
                        <button onClick={handleDeleteCategory}>delete</button>
                    </div>
                </div>
            </div>
        )}
    </div>
    )
}

/**
 *<div className="category-title-container">
                    <label htmlFor="title">Title</label>
                    <input 
                        id="title"
                        name="title"
                        type="text" 
                        placeholder='Homework'
                    />
                </div>
                <div className="category-color-container">
                    <select id="color" name="color">
                        <option value="White">Color</option>
                        <option value="White">White</option>
                        <option value="Red">Red</option>
                        <option value="Blue">Blue</option>
                        <option value="Green">Green</option>
                        <option value="Orange">Orange</option>
                    </select>
                </div>
                <div className="category-buttons-container">
                    <button>create</button>
                    <button onClick={toggleAddCat}>cancel</button>
                </div>
 */