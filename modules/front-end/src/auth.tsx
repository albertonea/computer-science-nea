import * as React from 'react'
import {login, Auth, logout, refreshToken, registerAccount} from "@/api/auth.ts";
import {useCallback, useEffect} from "react";
import {mergeLeft} from "ramda";

export type AuthContext = {
    isAuthenticated: boolean
    loginAndSaveContents: (username: string, password: string) => Promise<void>
    logoutAndDeleteLocalstorage: () => Promise<void>
    register: (username: string, password: string) => Promise<void>
    auth: Auth | null
}

const AuthContext = React.createContext<AuthContext | null>(null)

const key = 'auth'

function getStoredAuth(): Auth | null {
    const authJson = localStorage.getItem(key)
    if (authJson) {
        return JSON.parse(authJson)
    }
    return null
}

function setStoredAuth(auth: Auth | null) {
    if (auth) {
        localStorage.setItem(key, JSON.stringify(auth))
    } else {
        localStorage.removeItem(key)
    }
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [auth, setAuth] = React.useState<Auth | null>(getStoredAuth())
    const isAuthenticated = !!auth

    const logoutAndDeleteLocalstorage = useCallback(async () => {
        if (auth?.refreshToken) {
            await logout(auth.refreshToken)
        }
        setStoredAuth(null)
        setAuth(null)
    }, [])

    const loginAndSaveContents = useCallback(async (username: string, password: string) => {
        const loginResponse = await login(username, password);
        setStoredAuth(loginResponse)
        setAuth(loginResponse)
    }, [])

    const register = useCallback(async (username: string, password: string) => {
        await registerAccount(username, password)
    }, [])

    const refreshTokenAndSave = useCallback(async () => {
        console.log('refresh token')
        const auth = getStoredAuth()
        if (auth && new Date(auth.expiresAt).getTime() > new Date().getTime()) {
            const newAuthTokens = await refreshToken(auth.refreshToken)
            setStoredAuth(mergeLeft(newAuthTokens, auth))
        } else {
            await logoutAndDeleteLocalstorage()
        }
    }, [])

    useEffect(() => {
        setTimeout(async () => {
            await refreshTokenAndSave()
        }, 2700000)

        refreshTokenAndSave()
    })

    return (
        <AuthContext.Provider value={{ isAuthenticated, auth, register, loginAndSaveContents, logoutAndDeleteLocalstorage }}>
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
