package com.example.forum.core.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtil {
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isEmptyIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isEmptyIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isEmptyIp(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isEmptyIp(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isEmptyIp(ip)) {
            ip = request.getRemoteAddr();
            if (LOCALHOST_IP.equals(ip) || LOCALHOST_IPV6.equals(ip)) {
                ip = LOCALHOST_IP;
            }
        }
        if (ip != null && ip.length() > 15 && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    private static boolean isEmptyIp(String ip) {
        return ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip);
    }

    public static boolean isIpInRange(String ip, String cidr) {
        if (ip == null || cidr == null) {
            return false;
        }
        if (!cidr.contains("/")) {
            return ip.equals(cidr);
        }
        String[] parts = cidr.split("/");
        String network = parts[0];
        int prefix;
        try {
            prefix = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        long ipLong = ipToLong(ip);
        long networkLong = ipToLong(network);
        long mask = 0xFFFFFFFFL << (32 - prefix);
        return (ipLong & mask) == (networkLong & mask);
    }

    private static long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result = result << 8;
            try {
                result |= Integer.parseInt(parts[i]);
            } catch (Exception e) {
                result |= 0;
            }
        }
        return result;
    }
}
