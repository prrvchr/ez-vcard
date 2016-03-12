package ezvcard.io.json;

import static java.lang.Integer.parseInt;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.io.scribe.ScribeIndex;
import ezvcard.io.scribe.VCardPropertyScribe;
import ezvcard.property.VCardProperty;

/*
 Copyright (c) 2012-2016, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies, 
 either expressed or implied, of the FreeBSD Project.
 */

/**
 * Module for jackson-databind with JCardSerializer and JCardDeserializer as the
 * default serializers for {@link VCard}s
 * @author buddy
 */
public class JCardModule extends SimpleModule {
	private static final long serialVersionUID = 1L;

	public static final String MODULE_NAME = "JCardModule";
	private static final String[] V = Ezvcard.VERSION.split("[.-]");
	public static final Version MODULE_VERSION = new Version(parseInt(V[0]), parseInt(V[1]), parseInt(V[2]), V.length > 3 ? V[3] : "RELEASE", "com.googlecode.ez-vcard", "ez-vcard");

	private final JCardDeserializer deserializer = new JCardDeserializer();
	private final JCardSerializer serializer = new JCardSerializer();

	private ScribeIndex index;

	/**
	 * Creates a JCardModule, which can be registered with an ObjectMapper using
	 * {@link ObjectMapper#registerModule}
	 */
	public JCardModule() {
		super(MODULE_NAME, MODULE_VERSION);

		index = new ScribeIndex();
		serializer.setScribeIndex(index);
		deserializer.setScribeIndex(index);

		addSerializer(serializer);
		addDeserializer(VCard.class, deserializer);
	}

	/**
	 * Gets whether or not a "PRODID" property will be added to each vCard,
	 * saying that the vCard was generated by this library.
	 * @return true if the property will be added, false if not (defaults to
	 * true)
	 */
	public boolean isAddProdId() {
		return serializer.isAddProdId();
	}

	/**
	 * Sets whether or not to add a "PRODID" property to each vCard, saying that
	 * the vCard was generated by this library.
	 * @param addProdId true to add this property, false not to (defaults to
	 * true)
	 */
	public void setAddProdId(boolean addProdId) {
		serializer.setAddProdId(addProdId);
	}

	/**
	 * Gets whether properties that do not support the target version will be
	 * excluded from the written vCard.
	 * @return true to exclude properties that do not support the target
	 * version, false to include them anyway (defaults to true)
	 */
	public boolean isVersionStrict() {
		return serializer.isVersionStrict();
	}

	/**
	 * Sets whether properties that do not support the target version will be
	 * excluded from the written vCard.
	 * @param versionStrict true to exclude properties that do not support the
	 * target version, false to include them anyway (defaults to true)
	 */
	public void setVersionStrict(boolean versionStrict) {
		serializer.setVersionStrict(versionStrict);
	}

	/**
	 * <p>
	 * Registers a property scribe. This is the same as calling:
	 * </p>
	 * <p>
	 * {@code getScribeIndex().register(scribe)}
	 * </p>
	 * @param scribe the scribe to register
	 */
	public void registerScribe(VCardPropertyScribe<? extends VCardProperty> scribe) {
		index.register(scribe);
	}

	/**
	 * Gets the scribe index. Note that the same scribe index will be registered
	 * with both the serializer and the deserializer — thus registering a scribe
	 * with this index will register it with both.
	 * @return the scribe index
	 */
	public ScribeIndex getScribeIndex() {
		return index;
	}

	/**
	 * Sets the scribe index to be used by the serializer and deserializer.
	 * @param index the scribe index
	 */
	public void setScribeIndex(ScribeIndex index) {
		this.index = index;
		serializer.setScribeIndex(index);
		deserializer.setScribeIndex(index);
	}
}
