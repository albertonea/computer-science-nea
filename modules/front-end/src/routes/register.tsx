import {createFileRoute, useRouter, useRouterState} from '@tanstack/react-router'
import {useAuth} from "@/auth.tsx";
import {useState} from "react";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Label} from "@/components/ui/label.tsx";
import {Button} from "@/components/ui/button.tsx";

export const Route = createFileRoute('/register')({
  component: Register
})

function Register() {
  const auth = useAuth()
  const router = useRouter()
  const isLoading = useRouterState({ select: (s) => s.isLoading })
  const navigate = Route.useNavigate()
  const [isSubmitting, setIsSubmitting] = useState(false)

  const onFormSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    setIsSubmitting(true)
    try {
      e.preventDefault()
      const data = new FormData(e.currentTarget)
      const usernameFieldValue = data.get('username')
      const passwordFieldValue = data.get('password')
      const confirmPasswordFieldValue = data.get('confirmPassword')

      if (!usernameFieldValue || !passwordFieldValue || !confirmPasswordFieldValue) return
      const username = usernameFieldValue.toString()
      const password = passwordFieldValue.toString()
      const confirmPassword = confirmPasswordFieldValue.toString()

      if (confirmPassword === confirmPassword) {

        await auth.register(username, password)
        await router.invalidate()
        // This is just a hack being used to wait for the auth state to update
        // in a real app, you'd want to use a more robust solution
        await navigate({ to: '/login' })
      }
    } catch (error) {
      console.error('Error logging in: ', error)
    } finally {
      setIsSubmitting(false)
    }
  }

  const isLoggingIn = isLoading || isSubmitting


  return (
      <div className="flex w-full h-full justify-center items-center">
            <form onSubmit={onFormSubmit}>
                <fieldset disabled={isLoggingIn} className="w-full grid gap-2">
                    <Card>
                        <CardHeader>
                            <CardTitle className="text-2xl">Sign up</CardTitle>
                            <CardDescription>
                                Enter your username below to register to your account
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <div className="flex flex-col gap-6">
                                <div className="grid gap-2">
                                    <Label htmlFor="username">Username</Label>
                                    <Input
                                        id="username"
                                        type="username"
                                        name="username"
                                        placeholder="trader123"
                                        required
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <div className="flex items-center">
                                        <Label htmlFor="password">Password</Label>
                                    </div>
                                    <Input id="password" name="password" type="password" required />
                                </div>
                                <div className="grid gap-2">
                                  <div className="flex items-center">
                                    <Label htmlFor="confirmPassword">Confirm Password</Label>
                                  </div>
                                  <Input id="confirmPassword" name="confirmPassword" type="password" required />
                                </div>
                                <Button type="submit" className="w-full">
                                    Register
                                </Button>
                            </div>
                        </CardContent>
                    </Card>
                </fieldset>
            </form>
        </div>
  )
}
