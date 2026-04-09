package com.exam.exam_system.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.exam.exam_system.config.Messages;
import com.exam.exam_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Cached UserDetailsService — called on EVERY authenticated HTTP request
 * via JwtAuthenticationFilter → loadUserByUsername().
 *
 * WHY cache this:
 *   Without caching, every single API call triggers a DB query
 *   (findByUsername or findByEmail). This is the single highest-frequency
 *   read operation in the entire application.
 *
 * TTL: 2 minutes (configured in CacheConfig "userDetails" cache)
 *   Short enough to pick up deactivations/role changes quickly.
 *   Long enough to eliminate ~99% of redundant DB hits during a user session.
 *
 * Eviction: called from UserService when username, email, role,
 *   or active status changes.
 */
@Service
@RequiredArgsConstructor
public class CachedUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "userDetails", key = "#p0")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByUsernameWithPermissions(username)
                .or(() -> userRepository.findByEmailWithPermissions(username))
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
    }

    /**
     * Evict a specific user from the userDetails cache.
     * Called when username, email, role, or active status changes.
     */
    @CacheEvict(value = "userDetails", key = "#p0")
    public void evictUserDetails(String username) {
        // intentionally empty — annotation handles eviction
    }

    /**
     * Evict ALL entries from the userDetails cache.
     * Called on bulk operations (e.g., role permission changes affect all users with that role).
     */
    @CacheEvict(value = "userDetails", allEntries = true)
    public void evictAllUserDetails() {
        // intentionally empty — annotation handles eviction
    }
}
