import * as React from 'react'
import {loginRequest} from "@/api/users.ts";

type User = {
    userId: string,
    username: string,
    createdAt: Date
}

export interface AuthContext {
    isAuthenticated: boolean
    login: (username: string) => Promise<void>
    logout: () => Promise<void>
    user: User | null
}

const AuthContext = React.createContext<AuthContext | null>(null)

const key = 'auth.user'

function getStoredUser():User | null {
    const userJson = localStorage.getItem(key)
    if (userJson) {
        return JSON.parse(userJson)
    }
    return null
}

function setStoredUser(user: User | null) {
    if (user) {
        localStorage.setItem(key, JSON.stringify(user))
    } else {
        localStorage.removeItem(key)
    }
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [user, setUser] = React.useState<User | null>(getStoredUser())
    const isAuthenticated = !!user

    const logout = React.useCallback(async () => {
        setStoredUser(null)
        setUser(null)
    }, [])

    const login = React.useCallback(async (username: string) => {
        const user = await loginRequest(username);
        setStoredUser(user)
        setUser(user)
    }, [])

    React.useEffect(() => {
        setUser(getStoredUser())
    }, [])

    return (
        <AuthContext.Provider value={{ isAuthenticated, user, login, logout }}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth() {
    const context = React.useContext(AuthContext)
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider')
    }
    return context
}
