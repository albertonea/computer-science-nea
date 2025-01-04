#!/usr/bin/env pwsh
param(
    [Parameter(Position=0)]
    $command,
    $url,
    $userName,
    $password
)

function Show-Usage {
    Write-Host "Invalid parameters!"
    Write-Host "Usage:"
    Write-Host "`t./db.ps1 plan -url jdbc:postgresql://localhost:5432/dev -userName postgres -password postgres"
}

switch($command) {
    "plan" {
        flyway info -connectRetries=3 -url="$url" -user="$userName" -password="$password" -locations=migrations
        break
    }
    "apply" {
        flyway migrate -connectRetries=3 -url="$url" -user="$userName" -password="$password" -locations=migrations
        break
    }
    default {
        Show-Usage
    }
}