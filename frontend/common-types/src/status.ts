export function isClientError(status: number): boolean {
  return status >= 400 && status < 500;
}

export function isServerError(status: number): boolean {
  return status >= 500;
}

export function shouldAskForSignIn(status: number): boolean {
  return status === 401;
}

export function shouldShowAccessDenied(status: number): boolean {
  return status === 403;
}

export function shouldShowThrottleMessage(status: number): boolean {
  return status === 429;
}

export function shouldShowTechnicalMessage(status: number): boolean {
  return status >= 500;
}
