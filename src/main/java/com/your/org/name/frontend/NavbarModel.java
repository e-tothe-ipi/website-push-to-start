package com.your.org.name.frontend;

import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Collection;

/*
 *   From github.com/e-tothe-ipi
 *   Copyright (c) 2013, Benjamin Bercovitz
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *
 *       Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *       Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *   AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *   IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *   ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *   LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *   CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *   SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *   INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *   CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *   ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 *   THE POSSIBILITY OF SUCH DAMAGE.
 */

public class NavbarModel {

    private Collection<NavbarLinkModel> navbarLinks = new ArrayList<NavbarLinkModel>();
    private String brandName;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void addNavbarLink(String name, String path) {
        navbarLinks.add(new NavbarLinkModel(name, path));
    }

    public Collection<NavbarLinkModel> getNavbarLinks() {
        return navbarLinks;
    }

    public void setNavbarLinks(Collection<NavbarLinkModel> navbarLinks) {
        this.navbarLinks = navbarLinks;
    }

    public void activateLink(String name) {
        for (NavbarLinkModel l : navbarLinks) {
            if (l.getName().equals(HtmlUtils.htmlEscape(name))) {
                l.setActive(true);
            }
        }
    }


}
