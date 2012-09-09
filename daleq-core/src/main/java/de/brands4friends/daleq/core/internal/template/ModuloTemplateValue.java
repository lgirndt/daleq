/*
 * Copyright 2012 brands4friends, Private Sale GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.brands4friends.daleq.core.internal.template;

import java.math.BigInteger;

import com.google.common.base.Objects;

import de.brands4friends.daleq.core.TemplateValue;

final class ModuloTemplateValue implements TemplateValue {

    private final BigInteger modulus;

    public ModuloTemplateValue(final long modulus) {
        this.modulus = BigInteger.valueOf(modulus);
    }

    @Override
    public String render(final long value) {
        return BigInteger.valueOf(value).mod(modulus).toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ModuloTemplateValue) {
            final ModuloTemplateValue that = (ModuloTemplateValue) obj;

            return Objects.equal(modulus, that.modulus);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(modulus);
    }
}
