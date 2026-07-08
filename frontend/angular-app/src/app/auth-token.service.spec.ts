import { TestBed } from '@angular/core/testing';
import { AuthTokenService } from './auth-token.service';

describe('AuthTokenService', () => {
  let service: AuthTokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthTokenService);
  });

  it('stores and returns a token', () => {
    service.setToken('jwt-token');

    expect(service.getToken()).toBe('jwt-token');
  });

  it('clears the token', () => {
    service.setToken('jwt-token');

    service.clearToken();

    expect(service.getToken()).toBeUndefined();
  });
});
